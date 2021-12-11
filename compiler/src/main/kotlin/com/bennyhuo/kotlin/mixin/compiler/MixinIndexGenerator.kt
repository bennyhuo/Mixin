package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.addOriginatingElement
import com.bennyhuo.kotlin.mixin.annotations.MixinIndex
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec

/**
 * Created by benny.
 */
class MixinIndexGenerator {

    companion object {
        const val INDEX_PACKAGE = "com.bennyhuo.kotlin.mixin"
    }

    fun generate(env: XProcessingEnv, elements: Collection<XTypeElement>) {
        if (elements.isEmpty()) return

        val sortedElements = elements.sortedBy { it.qualifiedName }

        val indexName = "Mixin_${generateName(sortedElements)}"
        val typeSpec = TypeSpec.classBuilder(indexName)
            .addAnnotation(
                AnnotationSpec.builder(MixinIndex::class.java)
                    .addMember(
                        "value", "{${sortedElements.joinToString { "\$S" }}}",
                        *sortedElements.map { it.qualifiedName }.toTypedArray()
                    ).build()
            ).also { typeBuilder ->
                sortedElements.forEach {
                    typeBuilder.addOriginatingElement(it)
                }
            }
            .build()
        env.filer.write(JavaFile.builder(INDEX_PACKAGE, typeSpec).build(), XFiler.Mode.Aggregating)
    }

    private fun generateName(sortedElements: Collection<XTypeElement>): String {
        return sortedElements.joinToString("_") { it.qualifiedName }
            .replace('.', '_')
            .let {
                if (it.length > 200) {
                    it.substring(0, 200)
                } else it
            }
    }

}