package com.bennyhuo.kotlin.sample.compiler

import com.tschuchort.compiletesting.kspSourcesDir
import org.junit.Test

/**
 * Created by benny at 2021/6/21 7:00.
 */
class KspTest {

    fun doTest(path: String) =
        compilationWithKsp().let { compilation ->
            doTest(path, compilation, compilation.kspSourcesDir)
        }

    @Test
    fun testBasic() {
        doTest("testData/Basic.kt")
    }

    @Test
    fun testGeneric() {
        doTest("testData/Generics.kt")
    }
}