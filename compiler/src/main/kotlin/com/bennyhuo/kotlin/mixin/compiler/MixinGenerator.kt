package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.*
import com.bennyhuo.kotlin.mixin.annotations.Mixin
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier
import javax.tools.Diagnostic

/**
 * Created by benny.
 */
class MixinGenerator {

    fun generate(env: XProcessingEnv, elements: List<XTypeElement>) {
        elements.sortedBy { it.qualifiedName }
            .groupingBy {
                it.getAnnotation(Mixin::class)!!.value.let {
                    ClassName.get(it.packageName, it.name)
                }
            }.aggregate { key, accumulator: TypeHub?, element, first ->
                val acc = accumulator ?: TypeHub(
                    TypeSpec.classBuilder(key.simpleName().capitalize())
                        .addModifiers(Modifier.PUBLIC),
                    MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                )

                acc.typeBuilder.addTypeVariables(element.type.typeArguments.map {
                    it.typeName as TypeVariableName
                })

                val fieldName = element.name.decapitalize()
                acc.typeBuilder.addField(
                    FieldSpec.builder(
                        element.type.typeName,
                        fieldName,
                        Modifier.PRIVATE,
                        Modifier.FINAL
                    ).build()
                )

                acc.typeBuilder.addOriginatingElement(element)
                element.getDeclaredMethods()
                    .sortedBy { it.name }
                    .filter {
                        it.isPublic()
                    }.forEach { methodElement ->
                        if (methodElement.isSuspendFunction() &&
                            // X Processing will mistake the property flags for the synthetic getter function flags
                            // if isSuspendFunction return true, it may also be the getter of a val property(i.e. 'val url: String = ...') 
                            methodElement.returnType.typeName == TypeName.OBJECT
                        ) {
                            throw UnsupportedOperationException("Suspend function [$methodElement:${methodElement.javaClass}] is not supported.")
                        }


                        val methodId = methodElement.id
                        
                        val previousMethodBuilder = acc.methodBuilders[methodId]
                        val previousMethodSpec = previousMethodBuilder?.build()
                        val methodBuilder = when {
                            previousMethodBuilder == null -> {
                                MethodSpec.methodBuilder(methodElement.name)
                                    .addModifiers(Modifier.PUBLIC)
                                    .addAnnotations(
                                        methodElement.getAllAnnotations().map {
                                            AnnotationSpec.builder(it.type.typeName as ClassName)
                                                .also { annotationBuilder ->
                                                    it.annotationValues.map {
                                                        annotationBuilder.addMember(
                                                            it.name,
                                                            mapFormat(it.value),
                                                            *mapValue(it.value)
                                                        )
                                                    }
                                                }.build()
                                        }
                                    ).returns(methodElement.returnType.typeName)
                                    .also {
                                        if (methodElement.isStatic()) {
                                            it.addModifiers(Modifier.STATIC)
                                        }
                                        acc.methodBuilders[methodId] = it
                                    }
                            }
                            methodElement.returnType.isVoid() && previousMethodSpec?.returnType == TypeName.VOID-> {
                                previousMethodBuilder
                            }
                            else -> {
                                throw UnsupportedOperationException(
                                    "Duplicated method '${methodElement.name}' with different return types"
                                            + " '${methodElement.returnType.typeName}' and '${previousMethodSpec?.returnType}'."
                                            + "It is possible to mixin methods only if all of the return types are void."
                                )
                            }
                        }
                        
                        val args = StringBuilder()
                        methodElement.parameters.forEach { parameterElement ->
                            methodBuilder.addParameter(
                                parameterElement.type.typeName,
                                parameterElement.name
                            )
                            args.append(parameterElement.name).append(",")
                        }

                        val returnLiteral = if (methodElement.returnType.isVoid()) {
                            ""
                        } else {
                            "return"
                        }

                        methodBuilder.addStatement(
                            "$returnLiteral $fieldName.${methodElement.name}(\$L)",
                            args.removeSuffix(",")
                        )
                    }

                acc.typeBuilder.typeVariables.groupBy { it.name }
                    .forEach { (name, typeVariables) ->
                        if (typeVariables.size > 1) {
                            throw IllegalArgumentException("Duplicated type variable $name")
                        }
                    }

                val constructorArgs = StringBuilder()
                (element.findPrimaryConstructor() ?: element.getConstructors()
                    .maxByOrNull { it.parameters.size })?.parameters?.forEach {
                    acc.constructorBuilder.addParameter(it.type.typeName, it.name)
                    constructorArgs.append(it.name).append(",")
                }

                acc.constructorBuilder.addStatement(
                    "$fieldName = new \$T(\$L)",
                    element.type.typeName,
                    constructorArgs.removeSuffix(",")
                )
                acc
            }.map {
                JavaFile.builder(it.key.packageName(), it.value.build()).build()
                    .let {
                        env.filer.write(it, XFiler.Mode.Aggregating)
                    }
            }
    }

    class TypeHub(
        val typeBuilder: TypeSpec.Builder,
        val constructorBuilder: MethodSpec.Builder,
        val methodBuilders: MutableMap<String, MethodSpec.Builder> = HashMap()
    ) {
        fun build(): TypeSpec {
            return typeBuilder.addMethod(constructorBuilder.build())
                .addMethods(methodBuilders.values.map { it.build() })
                .build()
        }
    }

}