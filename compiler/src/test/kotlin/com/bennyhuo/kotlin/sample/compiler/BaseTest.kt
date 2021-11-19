package com.bennyhuo.kotlin.sample.compiler

import org.junit.Test

/**
 * Created by benny.
 */
abstract class BaseTest {

    abstract fun doTest(path: String)

    @Test
    fun testBasic() {
        doTest("testData/Basic.kt")
    }

    @Test
    fun testGeneric() {
        doTest("testData/Basic.kt")
    }

    @Test
    fun testModules() {
        doTest("testData/Modules.kt")
    }
}