package com.bennyhuo.kotlin.mixin.annotations

/**
 * Created by benny.
 */
@Target(AnnotationTarget.CLASS)
annotation class Mixin(
    val packageName: String,
    val name: String
)