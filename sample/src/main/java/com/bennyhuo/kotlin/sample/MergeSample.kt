package com.bennyhuo.kotlin.sample

import com.bennyhuo.kotlin.sample.annotations.Merge

/**
 * Created by benny.
 */
@Merge("xyz")
class X(val x0: Int, val x1: String) {
    fun x2() {
        println("Hello X")
    }
} 

@Merge("xyz")
class Y(val y0: IntArray, val y1: Array<String>) {
    fun y2() {
        println("Hello Y")
    }
}

@Merge("xyz")
class Z {
    fun z0() {
        println("Hello Z")
    }
    
    fun z1(value: String): String = value
}

@Merge("mn")
class M<T, P> {
    fun m0() {
        println("Hello m")
    }
    
    fun m1(value: String): String = value
    
    fun m2(value: T) {
        
    }
    
    fun m3(): P? = null
}

@Merge("mn")
class N<X: Number>(val n0: IntArray, val n1: Array<String>) {
    fun n2() {
        println("Hello Y")
    }
    
    fun n3(value: X) {
        println(value)
    }
}