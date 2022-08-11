package com.bennyhuo.kotlin.processor.module

/**
 * Created by benny.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class LibraryIndex(val value: Array<String>)