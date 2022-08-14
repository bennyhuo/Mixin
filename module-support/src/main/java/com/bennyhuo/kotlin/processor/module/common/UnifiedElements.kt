package com.bennyhuo.kotlin.processor.module.common

import androidx.room.compiler.processing.XElement
import com.google.devtools.ksp.symbol.KSAnnotated
import javax.lang.model.element.Element

/**
 * Created by benny.
 */
internal interface UniElement {
    val enclosedElements: List<UniElement>

    val isClassOrInterface: Boolean

    val isExecutable: Boolean

    val annotations: List<String>

    val enclosingTypeName: String

    /**
     * APT: Element
     * KSP: KSAnnotated
     * X: XElement
     */
    val rawElement: Any

    fun unwrapKsp(): KSAnnotated = rawElement as KSAnnotated

    fun unwrapApt(): Element = rawElement as Element

    fun unwrapXProcessing(): XElement = rawElement as XElement
}

internal interface UniTypeElement : UniElement {

    val qualifiedName: String

}

internal interface UniExecutableElement : UniElement {
    val parameters: List<UniElement>
}