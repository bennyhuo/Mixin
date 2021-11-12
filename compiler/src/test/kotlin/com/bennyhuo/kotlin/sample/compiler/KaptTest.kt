package com.bennyhuo.kotlin.sample.compiler

import org.junit.Test

/**
 * Created by benny at 2021/6/21 7:00.
 */
class KaptTest {
    
    fun doTest(path: String) = doTest(path, compilationWithKapt())
    
    @Test
    fun testBasic() {
        doTest("testData/Basic.kt")
    }
}