package com.bennyhuo.kotlin.mixin.compiler

import com.bennyhuo.kotlin.compiletesting.extensions.module.KotlinModule
import com.bennyhuo.kotlin.compiletesting.extensions.module.checkResult
import com.bennyhuo.kotlin.compiletesting.extensions.source.FileBasedModuleInfoLoader
import com.bennyhuo.kotlin.compiletesting.extensions.source.SourceModuleInfo

/**
 * Created by benny.
 */
fun doTest(path: String, creator: (SourceModuleInfo) -> KotlinModule) {
    val moduleInfoLoader = FileBasedModuleInfoLoader(path)

    val testModuleInfos = moduleInfoLoader.loadSourceModuleInfos()

    val testModules = testModuleInfos.map { creator(it) }
    testModules.checkResult(
        moduleInfoLoader.loadExpectModuleInfos(),
        checkGeneratedFiles = true
    )

}