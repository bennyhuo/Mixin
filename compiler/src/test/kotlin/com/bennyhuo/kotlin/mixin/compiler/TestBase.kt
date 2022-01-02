package com.bennyhuo.kotlin.mixin.compiler

import com.tschuchort.compiletesting.KotlinCompilation
import java.io.File
import kotlin.test.assertEquals

/**
 * Created by benny.
 */
const val SOURCE_START_LINE = "// SOURCE"
const val GENERATED_START_LINE = "// GENERATED"
val FILE_NAME_PATTERN = Regex("""// FILE: ((\w+)\.(\w+))\s*""")
val MODULE_NAME_PATTERN = Regex("""// MODULE: ([-\w]+)(\s*/\s*(([-\w]+)(\s*,\s*([-\w]+))*))?\s*(#(.*))?""")

const val DEFAULT_MODULE = "default-module"
const val DEFAULT_FILE = "DefaultFile.kt"

class ModuleInfo(
    val name: String,
    val args: Map<String, String> = emptyMap(),
    val dependencies: List<String> = emptyList(),
    val sourceFileInfos: MutableList<SourceFileInfo> = ArrayList()
)

class SourceFileInfo(val fileName: String) {
    val sourceBuilder = StringBuilder()

    override fun toString(): String {
        return "$fileName: \n$sourceBuilder"
    }
}

fun parseSourceData(lines: List<String>): List<ModuleInfo> {
    return lines.fold(ArrayList()) { moduleInfos, line ->
        val moduleResult = MODULE_NAME_PATTERN.find(line)
        if (moduleResult == null) {
            if (moduleInfos.isEmpty()) {
                moduleInfos += ModuleInfo(DEFAULT_MODULE)
            }

            val result = FILE_NAME_PATTERN.find(line)
            if (result == null) {
                val currentModule = moduleInfos.last()
                if (currentModule.sourceFileInfos.isEmpty()) {
                    moduleInfos.last().sourceFileInfos += SourceFileInfo(DEFAULT_FILE)
                }
                // append line to current source file
                currentModule.sourceFileInfos.last().sourceBuilder.append(line).appendLine()
            } else {
                // find new source file
                moduleInfos.last().sourceFileInfos += SourceFileInfo(result.groupValues[1])
            }
        } else {
            val dependencies = if (moduleResult.groupValues.size > 4) {
                moduleResult.groupValues[3].split(",")
                    .mapNotNull { it.trim().takeIf { it.isNotBlank() } }
            } else emptyList()

            val args = if (moduleResult.groupValues.size > 8) {
                moduleResult.groupValues[8].split(",").mapNotNull {
                    it.trim().split(":").takeIf { it.size == 2 }
                }.associate { it[0] to it[1] }
            } else emptyMap()

            moduleInfos += ModuleInfo(
                name = moduleResult.groupValues[1],
                args = args,
                dependencies = dependencies
            )
        }
        moduleInfos
    }
}

fun doTest(path: String, creator: (ModuleInfo) -> Module) {
    val lines = File(path).readLines()
        .dropWhile { it.trim() != SOURCE_START_LINE }
    val sourceLines =
        lines.takeWhile { it.trim() != GENERATED_START_LINE }.drop(1)
    val generatedLines =
        lines.dropWhile { it.trim() != GENERATED_START_LINE }.drop(1)

    val testModuleInfos = parseSourceData(sourceLines)
    val generatedModuleInfos = parseSourceData(generatedLines)

    val testModules = testModuleInfos.map { creator(it) }
    testModules.resolveAllDependencies()
    testModules.compileAll()

    val generatedSourceMap = generatedModuleInfos.flatMap { moduleInfo ->
        moduleInfo.sourceFileInfos.map {
            "${moduleInfo.name}:${it.fileName}" to it
        }
    }.toMap()

    val contentCollector = StringBuilder() to StringBuilder()
    testModules.forEach { module ->
        contentCollector.first.append("// MODULE: ${module.name}\n")
        contentCollector.second.append("// MODULE: ${module.name}\n")
        module.generatedSourceDir.walkTopDown()
            .filter { !it.isDirectory }
            .forEach {
                contentCollector.first.append("// FILE: ${it.name}\n")
                    .append(generatedSourceMap["${module.name}:${it.name}"]?.sourceBuilder.toString())

                contentCollector.second.append("// FILE: ${it.name}\n")
                    .append(it.readText())
            }

        assertEquals(module.compileResult?.exitCode, KotlinCompilation.ExitCode.OK)
    }

    assertEquals(contentCollector.first.toString(), contentCollector.second.toString())
}