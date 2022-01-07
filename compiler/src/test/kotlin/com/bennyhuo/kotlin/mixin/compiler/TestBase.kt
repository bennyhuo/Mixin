package com.bennyhuo.kotlin.mixin.compiler

import com.bennyhuo.kotlin.compiletesting.extensions.module.KotlinModule
import com.bennyhuo.kotlin.compiletesting.extensions.module.compileAll
import com.bennyhuo.kotlin.compiletesting.extensions.module.resolveAllDependencies
import com.bennyhuo.kotlin.compiletesting.extensions.source.SingleFileModuleInfoLoader
import com.bennyhuo.kotlin.compiletesting.extensions.source.SourceModuleInfo
import com.tschuchort.compiletesting.KotlinCompilation
import kotlin.test.assertEquals

/**
 * Created by benny.
 */
fun doTest(path: String, creator: (SourceModuleInfo) -> KotlinModule) {
    val moduleInfoLoader = SingleFileModuleInfoLoader(path)

    val testModuleInfos = moduleInfoLoader.loadSourceModuleInfos()
    val generatedModuleInfos = moduleInfoLoader.loadExpectModuleInfos()

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