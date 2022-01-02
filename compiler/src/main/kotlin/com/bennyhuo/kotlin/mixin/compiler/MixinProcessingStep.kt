package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.*
import com.bennyhuo.kotlin.mixin.annotations.Mixin

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
        val isLibraryModule = env.options["mixin.library"]?.toBoolean() ?: false
        val elements = elementsByAnnotation[Mixin::class.java.name]
            ?.filterIsInstance<XTypeElement>()
            ?: return emptySet()

        if (isLibraryModule) {
            MixinIndexGenerator().generate(env, elements)
        } else {
            MixinGenerator().generate(env, elements + MixinIndexLoader().loadIndex(env))
        }
        return emptySet()
    }
}