package com.bennyhuo.kotlin.mixin.annotations

/**
 * Created by benny.
 */
@Target(AnnotationTarget.CLASS)
annotation class MixinIndex(val value: Array<String>)
