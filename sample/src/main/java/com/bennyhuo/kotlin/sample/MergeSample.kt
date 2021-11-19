package com.bennyhuo.kotlin.sample

import com.bennyhuo.kotlin.sample.annotations.Composite

/**
 * Created by benny.
 */
@Composite("xyz")
class X(val x0: Int, val x1: String) {
    fun x2() {
        println("Hello X")
    }
} 

@Composite("xyz")
class Y(val y0: IntArray, val y1: Array<String>) {
    fun y2() {
        println("Hello Y")
    }
}

@Composite("xyz")
class Z {
    fun z0() {
        println("Hello Z")
    }
    
    fun z1(value: String): String = value
}

@Composite("mn")
class M<T, P> {
    @A
    fun m0() {
        println("Hello m")
    }

    @B(1, "Hello")
    fun m1(value: String): String = value

    @C([1,2,3], ["Hello", "World"])
    fun m2(value: T) {
        
    }
    
    fun m3(): P? = null
}

@Composite("mn")
class N<X: Number>(val n0: IntArray, val n1: Array<String>) {
    fun n2() {
        println("Hello Y")
    }
    
    fun n3(value: X) {
        println(value)
    }
}