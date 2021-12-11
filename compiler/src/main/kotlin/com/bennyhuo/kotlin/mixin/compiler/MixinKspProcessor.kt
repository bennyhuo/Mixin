package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.ksp.KspBasicAnnotationProcessor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class MixinKspProcessor(
    environment: SymbolProcessorEnvironment
) : KspBasicAnnotationProcessor(environment) {
    init {
        
    }
    override fun processingSteps() = listOf(
        MixinProcessingStep()
    )

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return MixinKspProcessor(environment)
        }
    }
}