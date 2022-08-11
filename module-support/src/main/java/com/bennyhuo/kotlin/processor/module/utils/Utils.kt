package com.bennyhuo.kotlin.processor.module.utils

import androidx.room.compiler.processing.XTypeElement
import java.math.BigInteger
import java.security.MessageDigest


/**
 * Created by benny.
 */
fun String.md5(): String {
    val md5 = MessageDigest.getInstance("MD5")
    return String.format("%032x", BigInteger(1, md5.digest(toByteArray())))
}

fun generateName(sortedElements: Collection<String>): String {
    return sortedElements.joinToString("_").md5()
}