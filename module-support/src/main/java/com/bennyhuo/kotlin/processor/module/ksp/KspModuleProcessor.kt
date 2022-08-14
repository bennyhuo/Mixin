package com.bennyhuo.kotlin.processor.module.ksp

import com.bennyhuo.kotlin.processor.module.utils.OPTION_KEY_LIBRARY
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Created by benny.
 */
abstract class KspModuleProcessor(
    val env: SymbolProcessorEnvironment
) : SymbolProcessor {

    abstract val annotations: Set<String>

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val elementsByAnnotation = annotations.associateWith { resolver.getSymbolsWithAnnotation(it).toSet() }
        val isMainModule = env.options[OPTION_KEY_LIBRARY].toBoolean()
        return if (isMainModule) {
            val elementsFromLibrary = KspIndexLoader(resolver, annotations).loadUnwrapped()
            processMain(resolver, elementsByAnnotation.mapValues {
                it.value + elementsFromLibrary.getOrDefault(it.key, emptySet())
            })
        } else {
            processLibrary(resolver, elementsByAnnotation)
        }
    }

    abstract fun processMain(resolver: Resolver, elementsByAnnotation: Map<String, Set<KSAnnotated>>): List<KSAnnotated>

    open fun processLibrary(
        resolver: Resolver,
        elementsByAnnotation: Map<String, Set<KSAnnotated>>
    ): List<KSAnnotated> {
        KspIndexGenerator(env).generate(elementsByAnnotation.values.flatMap {
            it.map { it.toUniElement() }
        })
        return emptyList()
    }

}