package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.*
import com.bennyhuo.kotlin.mixin.annotations.Mixin
import com.bennyhuo.kotlin.processor.module.xprocessing.XProcessingModuleStep

/**
 * Created by benny.
 */
class MixinProcessingStep : XProcessingModuleStep() {

    override val optionKeyPrefix = "mixin"

    override fun annotations(): Set<String> {
        return setOf(Mixin::class.java.name)
    }

    override fun processMain(env: XProcessingEnv, elementsByAnnotation: Map<String, Set<XElement>>): Set<XElement> {
        val elements = elementsByAnnotation[Mixin::class.java.name]
            ?.filterIsInstance<XTypeElement>()
            ?: return emptySet()
        MixinGenerator().generate(env, elements)
        return emptySet()
    }
}