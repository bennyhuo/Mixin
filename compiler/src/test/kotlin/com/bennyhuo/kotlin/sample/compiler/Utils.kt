package com.bennyhuo.kotlin.sample.compiler

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.jetbrains.kotlin.codegen.generateParameterNames
import java.io.File
import kotlin.test.assertEquals

/**
 * Created by benny.
 */
const val SOURCE_START_LINE = "// SOURCE"
const val GENERATED_START_LINE = "// GENERATED"
val FILE_NAME_PATTERN = Regex("""// FILE: ((\w+)\.(\w+))\s*""")
val MODULE_NAME_PATTERN = Regex("""// MODULE: ([-\w]+)(\s*/\s*(([-\w]+)(\s*,\s*([-\w]+))*))?""")

const val DEFAULT_MODULE = "default_module"
const val DEFAULT_FILE = "default_file.kt"

class SourceFileInfo(val module: String, val name: String, vararg val depends: String) {
    val sourceBuilder = StringBuilder()

    override fun toString(): String {
        return "$name: \n$sourceBuilder"
    }

    fun copy(
        module: String = this.module,
        name: String = this.name,
        vararg depends: String = this.depends
    ): SourceFileInfo {
        return SourceFileInfo(module, name, *depends)
    }
}

fun parseFiles(lines: List<String>): List<SourceFileInfo> {
    val sourceFileInfos = ArrayList<SourceFileInfo>()
    sourceFileInfos.add(SourceFileInfo(DEFAULT_MODULE, DEFAULT_FILE))
    lines.fold(sourceFileInfos) { acc, line ->
        val moduleResult = MODULE_NAME_PATTERN.find(line)
        if (moduleResult == null) {
            val result = FILE_NAME_PATTERN.find(line)
            if (result == null) {
                acc.last().sourceBuilder.append(line).appendLine()
            } else {
                acc.add(acc.last().copy(name = result.groupValues[1]))
            }
        } else {
            val depends = if (moduleResult.groupValues.size > 4) {
                moduleResult.groupValues[3].split(",")
                    .mapNotNull { it.trim().takeIf { it.isNotBlank() } }
            } else emptyList()
            sourceFileInfos.add(
                SourceFileInfo(
                    moduleResult.groupValues[1],
                    DEFAULT_FILE,
                    *depends.toTypedArray()
                )
            )
        }
        acc
    }
    return sourceFileInfos
}

fun doTest(path: String, creator: () -> CompileUnit) {
    val lines = File(path).readLines()
        .dropWhile { it.trim() != SOURCE_START_LINE }
    val sourceLines =
        lines.takeWhile { it.trim() != GENERATED_START_LINE }.drop(1)
    val generatedLines =
        lines.dropWhile { it.trim() != GENERATED_START_LINE }.drop(1)

    val sourceFileInfos = parseFiles(sourceLines)
    val generatedSourceFileInfos = parseFiles(generatedLines)

    val compileUnits = sourceFileInfos.groupBy {
        it.module
    }.mapValues {
        creator().also { unit ->
            unit.moduleName = it.key
            unit.compilation.sources = it.value.map { sourceFileInfo ->
                SourceFile.new(sourceFileInfo.name, sourceFileInfo.sourceBuilder.toString())
            }
            unit.dependencyNames += it.value.first().depends
        }
    }

    compileUnits.forEach { (_, unit) ->
        unit.resolveDependencies(compileUnits)
    }

    var left = compileUnits.values
    while (left.isNotEmpty()) {
        left.filter { it.canCompile() }.forEach { it.compile() }
        left = left.filter { !it.isCompiled }
    }
    
    val generatedSourceMap = generatedSourceFileInfos.associateBy { it.module }
    compileUnits.values.forEach { unit ->
        unit.generatedSourceDir.walkTopDown()
            .filter { !it.isDirectory }
            .forEach { 
                assertEquals(generatedSourceMap[unit.moduleName]?.sourceBuilder.toString(), it.readText())
            }

        assertEquals(unit.compileResult?.exitCode, KotlinCompilation.ExitCode.OK)
    }

//    val expectGenerateSource = generatedLines.joinToString("\n")
//
//    val generatedSource = generatedSourceFolder.walkTopDown()
//        .filter { !it.isDirectory }
//        .fold(StringBuilder()) { acc, it ->
//            acc.append("//-------${it.name}------\n")
//            acc.append(it.readText())
//            acc
//        }.toString()
//
//    assertEquals(expectGenerateSource, generatedSource)
}