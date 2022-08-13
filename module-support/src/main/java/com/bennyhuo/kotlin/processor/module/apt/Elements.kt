package com.bennyhuo.kotlin.processor.module.apt

import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.SimpleElementVisitor8

/**
 * Created by benny.
 */
fun Element.getEnclosingType(): TypeElement {
    return when (val element = enclosingElement) {
        is PackageElement -> throw IllegalArgumentException()
        is TypeElement -> element
        else -> element.getEnclosingType()
    }
}