package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.addOriginatingElement
import com.bennyhuo.kotlin.processor.module.common.IndexGeneratorForJava
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec

/**
 * Created by benny.
 */
internal class XProcessingIndexGenerator(
    private val env: XProcessingEnv
) : IndexGeneratorForJava {
    override fun writeType(typeSpec: TypeSpec) {
        env.filer.write(JavaFile.builder(PACKAGE_NAME, typeSpec).build(), XFiler.Mode.Aggregating)
    }
}