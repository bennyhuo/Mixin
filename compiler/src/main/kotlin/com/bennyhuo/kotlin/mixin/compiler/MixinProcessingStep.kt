package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.*
import com.bennyhuo.kotlin.mixin.annotations.Mixin
import com.squareup.javapoet.*
import javax.tools.Diagnostic

/**
 * Created by benny.
 */
class MixinProcessingStep : XProcessingStep {
    override fun annotations(): Set<String> {
        return setOf(Mixin::class.java.name)
    }

    override fun process(
        env: XProcessingEnv,
        elementsByAnnotation: Map<String, Set<XElement>>
    ): Set<XElement> {
        val isMainModule = env.options["mixin.main"]?.toBoolean() ?: false
        val elements = elementsByAnnotation[Mixin::class.java.name]
            ?.filterIsInstance<XTypeElement>()
            ?: return emptySet()
        
        env.messager.printMessage(Diagnostic.Kind.WARNING, "main: $isMainModule")
        if (isMainModule) {
            MixinGenerator().generate(env, elements + MixinIndexLoader().loadIndex(env))
        } else {
            MixinIndexGenerator().generate(env, elements)
        }
        return emptySet()
    }
}