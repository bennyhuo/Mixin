package com.bennyhuo.kotlin.sample.compiler

import androidx.room.compiler.processing.*
import com.bennyhuo.kotlin.sample.annotations.Composite
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier
import androidx.room.compiler.processing.isVoid

/**
 * Created by benny.
 */
class SampleProcessingStep : XProcessingStep {
    override fun annotations(): Set<String> {
        return setOf(Composite::class.java.name)
    }

    override fun process(
        env: XProcessingEnv,
        elementsByAnnotation: Map<String, Set<XElement>>
    ): Set<XElement> {
        val elements =
            elementsByAnnotation[Composite::class.java.name] ?: return emptySet()

        elements.filterIsInstance<XTypeElement>()
            .sortedBy { it.qualifiedName }
            .groupingBy {
                it.getAnnotation(Composite::class)!!.value.let {
                    ClassName.get(it.packageName, it.name)
                }
            }.aggregate { key, accumulator: TypeHub?, element, first ->
                val acc = accumulator ?: TypeHub(
                    TypeSpec.classBuilder(key.simpleName().capitalize()).addModifiers(Modifier.PUBLIC),
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

                element.getDeclaredMethods()
                    .sortedBy { it.name }
                    .filter {
                        it.isPublic()
                    }.forEach { methodElement ->
                        if (methodElement.isSuspendFunction())
                            throw UnsupportedOperationException("Suspend function is not supported.")

                        val method = MethodSpec.methodBuilder(methodElement.name)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(methodElement.returnType.typeName)
                            .also { builder ->
                                if (methodElement.isStatic()) {
                                    builder.addModifiers(Modifier.STATIC)
                                }

                                val args = StringBuilder()
                                methodElement.parameters.forEach { parameterElement ->
                                    builder.addParameter(
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

                                builder.addStatement(
                                    "$returnLiteral $fieldName.${methodElement.name}(\$L)",
                                    args.removeSuffix(",")
                                )
                            }.build()

                        acc.typeBuilder.addMethod(method)
                            .addOriginatingElement(element)
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
                        env.filer.write(it)
                    }
            }

        return emptySet()
    }

    class TypeHub(
        val typeBuilder: TypeSpec.Builder,
        val constructorBuilder: MethodSpec.Builder
    ) {
        fun build(): TypeSpec {
            return typeBuilder.addMethod(constructorBuilder.build()).build()
        }
    }
}