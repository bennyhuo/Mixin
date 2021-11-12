package com.bennyhuo.kotlin.sample.compiler

import androidx.room.compiler.processing.XProcessingStep
import androidx.room.compiler.processing.javac.JavacBasicAnnotationProcessor

/**
 * Created by benny.
 */
class SampleKaptProcessor : JavacBasicAnnotationProcessor() {
    override fun processingSteps() = listOf(SampleProcessingStep())
}