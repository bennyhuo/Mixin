package com.bennyhuo.kotlin.sample.annotations

/**
 * Created by benny.
 */
@Target(AnnotationTarget.CLASS)
annotation class Merge(
    val name: String,
    val packageName: String = "com.bennyhuo.kotlin.sample.annotations"
)