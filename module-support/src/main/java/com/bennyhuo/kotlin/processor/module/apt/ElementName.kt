package com.bennyhuo.kotlin.processor.module.apt

import javax.lang.model.element.ElementKind

/**
 * Created by benny.
 */
class ElementName(
    val name: String,
    val kind: ElementKind
)

fun String.toElementName(): ElementName {
    val parts = split("|")
    return ElementName(parts[0], ElementKind.valueOf(parts[1]))
}