package com.bennyhuo.kotlin.processor.module.apt

import com.bennyhuo.kotlin.processor.module.common.UniElement
import com.bennyhuo.kotlin.processor.module.common.UniExecutableElement
import com.bennyhuo.kotlin.processor.module.common.UniTypeElement
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

/**
 * Created by benny.
 */

internal fun Element.toUniElement(): UniElement = when (this) {
    is TypeElement -> toUniElement()
    is ExecutableElement -> toUniElement()
    else -> AptUniElement(this)
}

internal fun TypeElement.toUniElement(): UniTypeElement = AptUniTypeElement(this)
internal fun ExecutableElement.toUniElement(): UniExecutableElement = AptUniExecutableElement(this)

internal open class AptUniElement(
    override val rawElement: Element
) : UniElement {
    override val enclosedElements: List<UniElement>
        get() = rawElement.enclosedElements.map { it.toUniElement() }

    override val isClassOrInterface: Boolean
        get() = rawElement is TypeElement

    override val isExecutable: Boolean
        get() = rawElement is ExecutableElement

    override val annotations: List<String>
        get() = rawElement.annotationMirrors.map {
            (it.annotationType.asElement() as TypeElement).qualifiedName.toString()
        }.toList()

    override val enclosingTypeName: String
        get() = rawElement.getEnclosingType().qualifiedName.toString()

}

internal class AptUniTypeElement(
    override val rawElement: TypeElement
) : AptUniElement(rawElement), UniTypeElement {

    override val qualifiedName: String
        get() = rawElement.qualifiedName.toString()

}

internal class AptUniExecutableElement(
    override val rawElement: ExecutableElement
) : AptUniElement(rawElement), UniExecutableElement {

    override val parameters: List<UniElement>
        get() = rawElement.parameters.map { it.toUniElement() }

}