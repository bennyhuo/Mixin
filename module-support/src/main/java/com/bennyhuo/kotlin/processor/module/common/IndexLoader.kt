package com.bennyhuo.kotlin.processor.module.common

import com.bennyhuo.kotlin.processor.module.LibraryIndex

/**
 * Created by benny.
 */
internal interface IndexLoader {

    val annotations: Set<String>

    fun getTypeElement(typeName: String): UniTypeElement?

    fun findAnnotatedElementsByTypeName(enclosingTypeName: String): Collection<Pair<UniTypeElement, UniElement>> {
        val enclosingTypeElement = getTypeElement(enclosingTypeName)!!
        val annotationElements = annotations.mapNotNull { getTypeElement(it) }

        return findAnnotatedElements(enclosingTypeElement, annotationElements)
    }

    fun findAnnotatedElements(
        element: UniElement,
        annotationElements: Collection<UniTypeElement>
    ): Collection<Pair<UniTypeElement, UniElement>> {
        return element.enclosedElements.filter {
            // TypeElements are already found.
            !it.isClassOrInterface
        }.flatMap {
            findAnnotatedElements(it, annotationElements)
        } + ((element as? UniExecutableElement)?.parameters?.flatMap {
            findAnnotatedElements(it, annotationElements)
        } ?: emptyList()) + annotationElements.filter { annotationElement ->
            element.annotations.any { it == annotationElement.qualifiedName }
        }.map {
            it to element
        }
    }


    fun getIndexes(): List<LibraryIndex>

    fun load(): Map<UniTypeElement, List<UniElement>> {
        return getIndexes().flatMap {
            it.value.flatMap { findAnnotatedElementsByTypeName(it) }
        }.fold(HashMap<UniTypeElement, ArrayList<UniElement>>()) { acc, pair ->
            acc.also { map ->
                map.getOrPut(pair.first) { ArrayList() }.add(pair.second)
            }
        }
    }
}