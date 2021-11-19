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
    
    var moduleName = ""
    
    val classpaths = ArrayList<File>()

    val compilation = KotlinCompilation()
    
    val dependencyNames = ArrayList<String>()
    val dependencies = ArrayList<CompileUnit>()
    
    var isCompiled = false
    
    var compileResult: KotlinCompilation.Result? = null

    abstract val generatedSourceDir: File

    init {
        compilation.inheritClassPath = true
        compilation.classpaths = classpaths
    }

    private fun dependsOn(libraryCompilation: KotlinCompilation) {
        classpaths += libraryCompilation.classesDir
        classpaths += libraryCompilation.classpaths
    }

    fun dependsOn(libraryUnit: CompileUnit) {
        dependsOn(libraryUnit.compilation)
        dependencies += libraryUnit
    }
    
    fun resolveDependencies(compileUnits: Map<String, CompileUnit>) {
        dependencyNames.mapNotNull {
            compileUnits[it]
        }.forEach { 
            dependsOn(it)
        }
    }
    
    fun compile() {
        if (isCompiled) return
        isCompiled = true   
        
        compileResult = compilation.compile()
    }
    
    fun canCompile(): Boolean {
        return !isCompiled && dependencies.all { it.isCompiled }
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