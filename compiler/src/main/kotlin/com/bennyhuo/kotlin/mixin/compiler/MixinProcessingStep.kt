package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.*
import com.bennyhuo.kotlin.mixin.annotations.Mixin
import com.bennyhuo.kotlin.processor.module.common.MODULE_LIBRARY
import com.bennyhuo.kotlin.processor.module.common.MODULE_MAIN
import com.bennyhuo.kotlin.processor.module.xprocessing.XProcessingModuleStep

/**
 * Created by benny.
 */
class MixinProcessingStep : XProcessingModuleStep() {

    override val processorName: String = "mixin"

    override val supportedModuleTypes: Set<Int> = setOf(MODULE_MAIN, MODULE_LIBRARY)

    override fun annotations(): Set<String> {
        return setOf(Mixin::class.java.name)
    }

    override fun processMain(
        env: XProcessingEnv,
        elementsByAnnotation: Map<String, Set<XElement>>,
        elementsByAnnotationFromLibrary: Map<String, Set<XElement>>
    ): Set<XElement> {
        val elements = HashSet<XElement>()
        elementsByAnnotation[Mixin::class.java.name]?.let { elements.addAll(it) }
        elementsByAnnotationFromLibrary[Mixin::class.java.name]?.let { elements.addAll(it) }
        val typeElements = elements.takeIf { it.isNotEmpty() }
            ?.filterIsInstance<XTypeElement>()
            ?: return emptySet()
        MixinGenerator().generate(env, typeElements)
        return emptySet()
    }
}