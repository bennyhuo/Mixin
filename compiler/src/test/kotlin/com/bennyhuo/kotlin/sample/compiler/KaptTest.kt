package com.bennyhuo.kotlin.sample.compiler

import com.tschuchort.compiletesting.kspSourcesDir
import org.junit.Test

/**
 * Created by benny at 2021/6/21 7:00.
 */
class KaptTest {

    fun doTest(path: String) = compilationWithKapt().let { compilation ->
        doTest(path, compilation, compilation.kaptSourceDir)
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
