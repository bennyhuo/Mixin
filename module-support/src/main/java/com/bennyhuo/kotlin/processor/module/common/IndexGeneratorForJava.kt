package com.bennyhuo.kotlin.processor.module.common

import com.bennyhuo.kotlin.processor.module.LibraryIndex
import com.bennyhuo.kotlin.processor.module.utils.generateName
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Element

internal interface IndexGeneratorForJava : IndexGenerator {

    fun writeType(typeSpec: TypeSpec)

    override fun generate(elements: Collection<UniElement>) {
        if (elements.isEmpty()) return

        val sortedElementNames = elements.map { it.enclosingTypeName }.distinct().sortedBy { it }

        val indexName = "LibraryIndex_${generateName(sortedElementNames)}"
        val typeSpec = TypeSpec.classBuilder(indexName)
            .addAnnotation(
                AnnotationSpec.builder(LibraryIndex::class.java)
                    .addMember(
                        "value", "{${sortedElementNames.joinToString { "\$S" }}}",
                        *sortedElementNames.toTypedArray()
                    ).build()
            ).also { typeBuilder ->
                elements.forEach {
                    typeBuilder.addOriginatingElement(it.rawElement as Element)
                }
            }
            .build()

        writeType(typeSpec)
    }

}