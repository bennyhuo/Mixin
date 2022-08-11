package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.addOriginatingElement
import com.bennyhuo.kotlin.processor.module.IndexGeneratorForJava
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec

/**
 * Created by benny.
 */
class XProcessingIndexGenerator(
    private val env: XProcessingEnv
) : IndexGeneratorForJava<XTypeElement> {

    override fun getElementName(element: XTypeElement): String {
        return element.qualifiedName
    }

    override fun addOriginatingElements(typeSpecBuilder: TypeSpec.Builder, elements: Collection<XTypeElement>) {
        elements.forEach {
            typeSpecBuilder.addOriginatingElement(it)
        }
    }

    override fun writeType(typeSpec: TypeSpec) {
        env.filer.write(JavaFile.builder(PACKAGE_NAME, typeSpec).build(), XFiler.Mode.Aggregating)
    }
}