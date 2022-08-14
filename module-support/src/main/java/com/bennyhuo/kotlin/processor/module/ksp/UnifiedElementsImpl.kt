package com.bennyhuo.kotlin.processor.module.ksp

import com.bennyhuo.kotlin.processor.module.common.UniElement
import com.bennyhuo.kotlin.processor.module.common.UniExecutableElement
import com.bennyhuo.kotlin.processor.module.common.UniTypeElement
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclarationContainer
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

/**
 * Created by benny.
 */

internal fun KSAnnotated.toUniElement(): UniElement = when (this) {
    is KSClassDeclaration -> toUniElement()
    is KSFunctionDeclaration -> toUniElement()
    else -> KspUniElement(this)
}

internal fun KSClassDeclaration.toUniElement(): UniTypeElement = KspUniTypeElement(this)
internal fun KSFunctionDeclaration.toUniElement(): UniExecutableElement = KspUniExecutableElement(this)

internal open class KspUniElement(
    override val rawElement: KSAnnotated
) : UniElement {
    override val enclosedElements: List<UniElement>
        get() = when (val ksAnnotated = rawElement) {
            is KSDeclarationContainer -> ksAnnotated.declarations.map { it.toUniElement() }.toList()
            else -> emptyList()
        }

    override val isClassOrInterface: Boolean
        get() = rawElement is KSClassDeclaration

    override val isExecutable: Boolean
        get() = rawElement is KSFunctionDeclaration

    override val annotations: List<String>
        get() = rawElement.annotations.map {
            it.annotationType.resolve().declaration.qualifiedName!!.asString()
        }.toList()

    override val enclosingTypeName: String
        get() = rawElement.getDeclarationName().toString()

}

internal class KspUniTypeElement(
    override val rawElement: KSClassDeclaration
) : KspUniElement(rawElement), UniTypeElement {

    override val qualifiedName: String
        get() = rawElement.qualifiedName!!.asString()

}

internal class KspUniExecutableElement(
    override val rawElement: KSFunctionDeclaration
) : KspUniElement(rawElement), UniExecutableElement {

    override val parameters: List<UniElement>
        get() = rawElement.parameters.map { it.toUniElement() }

}