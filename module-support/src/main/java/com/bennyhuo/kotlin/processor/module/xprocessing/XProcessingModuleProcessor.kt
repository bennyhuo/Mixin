package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XProcessingStep
import com.bennyhuo.kotlin.processor.module.utils.OPTION_KEY

/**
 * Created by benny.
 */
abstract class XProcessingModuleProcessor : XProcessingStep {

    override fun process(env: XProcessingEnv, elementsByAnnotation: Map<String, Set<XElement>>): Set<XElement> {
        val isMainModule = env.options[OPTION_KEY].toBoolean()
        return if (isMainModule) {
            val elementsFromLibrary = XProcessingIndexLoader(env, annotations()).load()
            processMain(env, elementsByAnnotation.mapValues {
                it.value + elementsFromLibrary.getOrDefault(it.key, emptySet())
            })
        } else {
            processLibrary(env, elementsByAnnotation)
        }
    }


    abstract fun processMain(env: XProcessingEnv, elementsByAnnotation: Map<String, Set<XElement>>): Set<XElement>

    open fun processLibrary(env: XProcessingEnv, elementsByAnnotation: Map<String, Set<XElement>>): Set<XElement> {
        XProcessingIndexGenerator(env).generate(elementsByAnnotation.values.flatten())
        return emptySet()
    }

}