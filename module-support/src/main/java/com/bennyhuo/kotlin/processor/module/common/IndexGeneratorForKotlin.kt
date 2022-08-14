package com.bennyhuo.kotlin.processor.module.common

import com.bennyhuo.kotlin.processor.module.LibraryIndex
import com.bennyhuo.kotlin.processor.module.utils.generateName
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile

internal interface IndexGeneratorForKotlin : IndexGenerator {
    fun writeType(typeSpec: TypeSpec)

    override fun generate(elements: Collection<UniElement>) {
        if (elements.isEmpty()) return

        val sortedElementNames = elements.map { it.enclosingTypeName }.sortedBy { it }

        val indexName = "LibraryIndex_${generateName(sortedElementNames)}"
        val typeSpec = TypeSpec.classBuilder(indexName)
            .addAnnotation(
                AnnotationSpec.builder(LibraryIndex::class.java)
                    .addMember(
                        "value", "{${sortedElementNames.joinToString { "\$S" }}}",
                        *sortedElementNames.toTypedArray()
                    ).build()
            ).also { typeBuilder ->
                elements.mapNotNull {
                    (it.rawElement as KSAnnotated).containingFile
                }.forEach {
                    typeBuilder.addOriginatingKSFile(it)
                }
            }
            .build()

        writeType(typeSpec)
    }

}
