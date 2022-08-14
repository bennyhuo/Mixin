package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XExecutableElement
import androidx.room.compiler.processing.XExecutableParameterElement
import androidx.room.compiler.processing.XFieldElement
import androidx.room.compiler.processing.XTypeElement

/**
 * Created by benny.
 */
internal fun XElement.getEnclosingTypeName(): String {
    return when (this) {
        is XTypeElement -> this.qualifiedName
        is XFieldElement -> enclosingElement.className.canonicalName()
        is XExecutableElement -> enclosingElement.className.canonicalName()
        is XExecutableParameterElement -> enclosingMethodElement.getEnclosingTypeName()
        else -> throw IllegalArgumentException()
    }
}