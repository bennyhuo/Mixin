package com.bennyhuo.kotlin.processor.module.apt

import com.bennyhuo.kotlin.processor.module.IndexGeneratorForJava
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * Created by benny.
 */
class AptIndexGenerator(
    private val env: ProcessingEnvironment
) : IndexGeneratorForJava<Element> {

    override fun getElementName(element: Element): String {
        return element.getEnclosingType().qualifiedName.toString()
    }

    override fun addOriginatingElements(typeSpecBuilder: TypeSpec.Builder, elements: Collection<Element>) {
        elements.forEach {
            typeSpecBuilder.addOriginatingElement(it)
        }
    }

    override fun writeType(typeSpec: TypeSpec) {
        JavaFile.builder(PACKAGE_NAME, typeSpec).build().writeTo(env.filer)
    }
}