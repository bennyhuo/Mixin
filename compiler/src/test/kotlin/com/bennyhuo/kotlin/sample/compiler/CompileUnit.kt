package com.bennyhuo.kotlin.sample.compiler

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import java.io.File
import javax.annotation.processing.AbstractProcessor

/**
 * Created by benny.
 */
abstract class CompileUnit {
    val classpaths = ArrayList<File>()

    val compilation = KotlinCompilation()

    abstract val generatedSourceDir: File

    init {
        compilation.inheritClassPath = true
        compilation.classpaths = classpaths
    }

    fun dependsOn(libraryCompilation: KotlinCompilation) {
        classpaths += libraryCompilation.classesDir
        classpaths += libraryCompilation.classpaths
    }

    fun dependsOn(libraryUnit: CompileUnit) {
        dependsOn(libraryUnit.compilation)
    }

}

class KspCompileUnit(kspProcessorProviders: List<SymbolProcessorProvider>) : CompileUnit() {

    init {
        compilation.symbolProcessorProviders = kspProcessorProviders
    }
    
    override val generatedSourceDir: File = compilation.kspSourcesDir
}

class KaptCompileUnit(kaptProcessors: List<AbstractProcessor>) : CompileUnit() {
    
    init {
        compilation.annotationProcessors = kaptProcessors
    }

    override val generatedSourceDir: File = compilation.kspSourcesDir
}