package com.bennyhuo.kotlin.sample.compiler

import androidx.room.compiler.processing.ksp.KspBasicAnnotationProcessor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class SampleKspProcessor(
    environment: SymbolProcessorEnvironment
) : KspBasicAnnotationProcessor(environment) {
    init {
        
    }
    override fun processingSteps() = listOf(
        SampleProcessingStep()
    )

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return SampleKspProcessor(environment)
        }
    }
}