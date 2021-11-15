package com.bennyhuo.kotlin.sample.compiler

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XProcessingStep
import javax.tools.Diagnostic

/**
 * Created by benny.
 */
class SampleProcessingStep : XProcessingStep {
    override fun annotations(): Set<String> {
        return setOf("com.bennyhuo.kotlin.sample.annotations.Foo")
    }

    override fun process(
        env: XProcessingEnv,
        elementsByAnnotation: Map<String, Set<XElement>>
    ): Set<XElement> {
        env.messager.printMessage(
            Diagnostic.Kind.WARNING,
            elementsByAnnotation.values.joinToString { it.joinToString { it.toString() } })
        return emptySet()
    }
}