package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.XAnnotation
import androidx.room.compiler.processing.XAnnotationValue
import androidx.room.compiler.processing.XMethodElement

/**
 * Created by benny.
 */ 
fun mapFormat(value: Any?): String {
    return when (value) {
        is String -> {
            "\$S"
        }
        is List<*> -> {
            "{ ${value.joinToString { mapFormat(it) }} }"
        }
        is XAnnotationValue -> {
            mapFormat(value.value)
        }
        is Annotation, is XAnnotation -> {
            throw UnsupportedOperationException("Annotations in Annotation is not supported.")
        }
        else -> {
            "\$L"
        }
    }
}
 
fun mapValue(value: Any?): Array<Any?> {
    return when (value) {
        is String -> {
            arrayOf(value)
        }
        is List<*> -> {
            value.flatMap { mapValue(it).toList() }.toTypedArray()
        }
        is XAnnotationValue -> {
            mapValue(value.value)
        }
        is Annotation, is XAnnotation -> {
            throw UnsupportedOperationException("Annotations in Annotation is not supported.")
        }
        else -> {
            // integer ...
            arrayOf(value)
        }
    }
}

val XMethodElement.id: String
    get() {
        return "$name(${parameters.joinToString { it.type.typeName.toString() }})"   
    }