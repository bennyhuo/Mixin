package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XExecutableElement
import androidx.room.compiler.processing.XTypeElement
import com.bennyhuo.kotlin.processor.module.common.UniElement
import com.bennyhuo.kotlin.processor.module.common.UniExecutableElement
import com.bennyhuo.kotlin.processor.module.common.UniTypeElement

/**
 * Created by benny.
 */

internal fun XElement.toUniElement(): UniElement = when (this) {
    is XTypeElement -> toUniElement()
    is XExecutableElement -> toUniElement()
    else -> XUniElement(this)
}

internal fun XTypeElement.toUniElement(): UniTypeElement = XUniTypeElement(this)
internal fun XExecutableElement.toUniElement(): UniExecutableElement = XUniExecutableElement(this)

internal open class XUniElement(
    override val rawElement: XElement
) : UniElement {
    override val enclosedElements: List<UniElement>
        get() = when(val element = rawElement) {
            is XTypeElement -> element.getEnclosedElements().map { it.toUniElement() }
            else -> emptyList()
        }

    override val isClassOrInterface: Boolean
        get() = rawElement is XTypeElement

    override val isExecutable: Boolean
        get() = rawElement is XExecutableElement

    override val annotations: List<String>
        get() = rawElement.getAllAnnotations().map {
            (it.type.typeElement as XTypeElement).qualifiedName
        }.toList()

    override val enclosingTypeName: String
        get() = rawElement.getEnclosingTypeName()

}

internal class XUniTypeElement(
    override val rawElement: XTypeElement
) : XUniElement(rawElement), UniTypeElement {

    override val qualifiedName: String
        get() = rawElement.qualifiedName

}

internal class XUniExecutableElement(
    override val rawElement: XExecutableElement
) : XUniElement(rawElement), UniExecutableElement {

    override val parameters: List<UniElement>
        get() = rawElement.parameters.map { it.toUniElement() }

}