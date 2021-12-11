package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.javac.JavacBasicAnnotationProcessor

/**
 * Created by benny.
 */
class MixinKaptProcessor : JavacBasicAnnotationProcessor() {
    override fun processingSteps() = listOf(MixinProcessingStep())
}