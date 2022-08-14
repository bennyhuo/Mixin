package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XProcessingStep
import com.bennyhuo.kotlin.processor.module.utils.OPTION_KEY_LIBRARY

/**
 * Created by benny.
 */
abstract class XProcessingModuleStep : XProcessingStep {

    open val optionKeyPrefix = "module"

    final override fun process(env: XProcessingEnv, elementsByAnnotation: Map<String, Set<XElement>>): Set<XElement> {
        val isMainModule = !env.options["${optionKeyPrefix}.${OPTION_KEY_LIBRARY}"].toBoolean()
        return if (isMainModule) {
            val elementsFromLibrary = XProcessingIndexLoader(env, annotations()).loadUnwrap()
            processMain(env, elementsByAnnotation.mapValues {
                it.value + elementsFromLibrary.getOrDefault(it.key, emptySet())
            })
        } else {
            processLibrary(env, elementsByAnnotation)
        }
    }


    abstract fun processMain(env: XProcessingEnv, elementsByAnnotation: Map<String, Set<XElement>>): Set<XElement>

    open fun processLibrary(env: XProcessingEnv, elementsByAnnotation: Map<String, Set<XElement>>): Set<XElement> {
        XProcessingIndexGenerator(env).generate(elementsByAnnotation.values.flatMap {
            it.map { it.toUniElement() }
        })
        return emptySet()
    }

}