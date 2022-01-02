package com.bennyhuo.kotlin.mixin.compiler

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.*
import java.io.File
import javax.annotation.processing.AbstractProcessor

/**
 * Created by benny.
 */
abstract class Module(
    val name: String,
    val args: Map<String, String>,
    val sourceFiles: List<SourceFile>,
    val dependencyNames: List<String>
) {
    val classpaths = ArrayList<File>()

    val dependencies = ArrayList<Module>()

    var isCompiled = false

    var compileResult: KotlinCompilation.Result? = null

    abstract val generatedSourceDir: File

    abstract val classesDir: File

    protected abstract fun setupArgs()

    val isReadyToCompile: Boolean
        get() {
            return !isCompiled && dependencies.all { it.isCompiled }
        }

    protected val compilation = newCompilation().also {
        it.sources = sourceFiles
    }

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

    fun resolveDependencies(moduleMap: Map<String, Module>) {
        dependencyNames.mapNotNull {
            moduleMap[it]
        }.forEach {
            dependsOn(it)
        }
    }

    open fun compile() {
        if (isCompiled) return
        ensureDependencies()

        isCompiled = true

        setupArgs()
        compileResult = compilation.compile()
    }

    private fun ensureDependencies() {
        dependencies.forEach {
            it.compile()
        }
    }

    override fun toString() =
        "$name: $isCompiled >> ${compileResult?.exitCode} ${compileResult?.messages}"

}

class KspModule(
    moduleName: String,
    args: Map<String, String>,
    sourceFiles: List<SourceFile>,
    dependencyNames: List<String>,
    vararg kspProcessorProviders: SymbolProcessorProvider
) : Module(moduleName, args, sourceFiles, dependencyNames) {

    init {
        compilation.symbolProcessorProviders += kspProcessorProviders
    }

    override val generatedSourceDir: File = compilation.kspSourcesDir

    private val realCompilation = newCompilation()
    override val classesDir: File = realCompilation.classesDir

    override fun setupArgs() {
        compilation.kspArgs.putAll(args)
        realCompilation.kspArgs.putAll(args)
    }

    override fun compile() {
        super.compile()

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
    args: Map<String, String>,
    sourceFiles: List<SourceFile>,
    dependencyNames: List<String>,
    vararg kaptProcessors: AbstractProcessor,
) : Module(moduleName, args, sourceFiles, dependencyNames) {

    init {
        compilation.annotationProcessors += kaptProcessors
    }

    override val generatedSourceDir: File = compilation.kaptSourceDir
    override val classesDir: File = compilation.classesDir

    override fun setupArgs() {
        compilation.kaptArgs.putAll(args)
    }
}

fun Collection<Module>.resolveAllDependencies() {
    val moduleMap = this.associateBy { it.name }
    forEach {
        it.resolveDependencies(moduleMap)
    }
}

fun Collection<Module>.compileAll() {
    forEach {
        it.compile()
    }
}