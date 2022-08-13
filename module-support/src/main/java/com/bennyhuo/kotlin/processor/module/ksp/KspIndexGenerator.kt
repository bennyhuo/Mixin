package com.bennyhuo.kotlin.processor.module.ksp

import com.bennyhuo.kotlin.processor.module.IndexGeneratorForKotlin
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.writeTo


/**
 * Created by benny.
 */
class KspIndexGenerator(
    private val env: SymbolProcessorEnvironment
) : IndexGeneratorForKotlin<KSAnnotated> {

    override fun getElementName(element: KSAnnotated): String {
        return element.getDeclarationName().toString()
    }

    override fun addOriginatingElements(typeSpecBuilder: TypeSpec.Builder, elements: Collection<KSAnnotated>) {
        elements.forEach {
            typeSpecBuilder.addOriginatingKSFile(it.containingFile!!)
        }
    }

    override fun writeType(typeSpec: TypeSpec) {
        FileSpec.builder(PACKAGE_NAME, typeSpec.name!!).build().writeTo(env.codeGenerator, true)
    }
}