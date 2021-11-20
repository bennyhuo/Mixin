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
abstract class Module(val name: String) {

    val classpaths = ArrayList<File>()

    val dependencyNames = ArrayList<String>()
    val dependencies = ArrayList<Module>()

    var isCompiled = false

    var compileResult: KotlinCompilation.Result? = null

    abstract val generatedSourceDir: File

    abstract val classesDir: File

    val isReadyToCompile: Boolean
        get() {
            return !isCompiled && dependencies.all { it.isCompiled }
        }

    protected val compilation = newCompilation()

    protected fun newCompilation(): KotlinCompilation {
        return KotlinCompilation().also { compilation ->
            compilation.inheritClassPath = true
            compilation.classpaths = classpaths
        }
    }

    private fun dependsOn(libraryUnit: Module) {
        classpaths += libraryUnit.classesDir
        classpaths += libraryUnit.classpaths

        dependencies += libraryUnit
    }

    fun addSourceFiles(sourceFiles: List<SourceFile>) {
        compilation.sources += sourceFiles
    }

    fun resolveDependencies(compileUnits: Map<String, Module>) {
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
        "$name: $isCompiled >> ${compileResult?.exitCode} ${compileResult?.messages}"

}

class KspModule(
    moduleName: String,
    vararg kspProcessorProviders: SymbolProcessorProvider
) : Module(moduleName) {

    init {
        compilation.symbolProcessorProviders += kspProcessorProviders
    }

    override val generatedSourceDir: File = compilation.kspSourcesDir

    private val realCompilation = newCompilation()
    override val classesDir: File = realCompilation.classesDir

    override fun compile() {
        if (isCompiled) return
        isCompiled = true

        compilation.compile()
        realCompilation.sources = compilation.sources + compilation.kspSourcesDir.walkTopDown()
            .filter { !it.isDirectory }
            .map {
                SourceFile.new(it.name, it.readText())
            }

        compileResult = realCompilation.compile()
    }
}

class KaptModule(
    moduleName: String,
    vararg kaptProcessors: AbstractProcessor
) : Module(moduleName) {

    init {
        compilation.annotationProcessors += kaptProcessors
    }

    override val generatedSourceDir: File = compilation.kaptSourceDir
    override val classesDir: File = compilation.classesDir
}