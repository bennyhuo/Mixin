package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XElement
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
) : IndexGeneratorForJava<XElement> {

    override fun getElementName(element: XElement): String {
        return element.getEnclosingTypeName()
    }

    override fun addOriginatingElements(typeSpecBuilder: TypeSpec.Builder, elements: Collection<XElement>) {
        elements.forEach {
            typeSpecBuilder.addOriginatingElement(it)
        }
    }

    override fun writeType(typeSpec: TypeSpec) {
        env.filer.write(JavaFile.builder(PACKAGE_NAME, typeSpec).build(), XFiler.Mode.Aggregating)
    }
}