package com.bennyhuo.kotlin.sample.compiler

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
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

    val compilation = newCompilation()

    val dependencyNames = ArrayList<String>()
    val dependencies = ArrayList<CompileUnit>()

    var isCompiled = false

    var compileResult: KotlinCompilation.Result? = null

    abstract val generatedSourceDir: File

    val isReadyToCompile: Boolean
        get() {
            return !isCompiled && dependencies.all { it.isCompiled }
        }

    protected fun newCompilation(): KotlinCompilation {
        return KotlinCompilation().also { compilation ->
            compilation.inheritClassPath = true
            compilation.classpaths = classpaths
        }
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

    open fun compile() {
        if (isCompiled) return
        isCompiled = true

        compileResult = compilation.compile()
    }

    override fun toString() =
        "$moduleName: $isCompiled >> ${compileResult?.exitCode} ${compileResult?.messages}"

}

class KspCompileUnit(vararg kspProcessorProviders: SymbolProcessorProvider) : CompileUnit() {

    init {
        compilation.symbolProcessorProviders += kspProcessorProviders
    }

    override val generatedSourceDir: File = compilation.kspSourcesDir

    override fun compile() {
        if (isCompiled) return
        isCompiled = true

        compilation.compile()

        val realCompilation = newCompilation()
        realCompilation.sources = compilation.sources + compilation.kspSourcesDir.walkTopDown()
            .filter { !it.isDirectory }
            .map {
                SourceFile.new(it.name, it.readText())
            }

        compileResult = realCompilation.compile()
    }
}

class KaptCompileUnit(vararg kaptProcessors: AbstractProcessor) : CompileUnit() {

    init {
        compilation.annotationProcessors += kaptProcessors
    }

    override val generatedSourceDir: File = compilation.kaptSourceDir
}