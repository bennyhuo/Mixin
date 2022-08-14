package com.bennyhuo.kotlin.processor.module.apt

import com.bennyhuo.kotlin.processor.module.common.IndexGeneratorForJava
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

/**
 * Created by benny.
 */
internal class AptIndexGenerator(
    private val env: ProcessingEnvironment
) : IndexGeneratorForJava {
    override fun writeType(typeSpec: TypeSpec) {
        JavaFile.builder(PACKAGE_NAME, typeSpec).build().writeTo(env.filer)
    }
}