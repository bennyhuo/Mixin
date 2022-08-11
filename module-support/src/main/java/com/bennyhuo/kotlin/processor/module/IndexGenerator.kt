package com.bennyhuo.kotlin.processor.module

import com.bennyhuo.kotlin.processor.module.utils.generateName
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.TypeSpec

/**
 * Created by benny.
 */

interface IndexGenerator<Element> {

    fun generate(elements: Collection<Element>)

}

interface IndexGeneratorForJava<Element> : IndexGenerator<Element> {

    fun getElementName(element: Element): String

    fun addOriginatingElements(typeSpecBuilder: TypeSpec.Builder, elements: Collection<Element>)

    fun writeType(typeSpec: TypeSpec)

    override fun generate(elements: Collection<Element>) {
        if (elements.isEmpty()) return

        val sortedElements = elements.sortedBy { getElementName(it) }

        val indexName = "LibraryIndex_${generateName(sortedElements.map { getElementName(it) })}"
        val typeSpec = TypeSpec.classBuilder(indexName)
            .addAnnotation(
                AnnotationSpec.builder(LibraryIndex::class.java)
                    .addMember(
                        "value", "{${sortedElements.joinToString { "\$S" }}}",
                        *sortedElements.map { getElementName(it) }.toTypedArray()
                    ).build()
            ).also { typeBuilder ->
                addOriginatingElements(typeBuilder, sortedElements)
            }
            .build()

        writeType(typeSpec)
    }

}

