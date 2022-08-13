package com.bennyhuo.kotlin.processor.module.apt

import com.bennyhuo.kotlin.processor.module.LibraryIndex
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

/**
 * Created by benny.
 */
class AptIndexLoader(
    private val env: ProcessingEnvironment,
    val annotations: Set<String>
) {

    fun findAnnotatedElementsByTypeName(enclosingTypeName: String): Collection<Pair<TypeElement, Element>> {
        val enclosingTypeElement = env.elementUtils.getTypeElement(enclosingTypeName)
        val annotationElements = annotations.mapNotNull { env.elementUtils.getTypeElement(it) }

        return findAnnotatedElements(enclosingTypeElement, annotationElements)
    }

    private fun findAnnotatedElements(
        element: Element,
        annotationElements: Collection<TypeElement>
    ): Collection<Pair<TypeElement, Element>> {
        return element.enclosedElements.filter {
            // TypeElements are already found.
            !it.kind.isClass && !it.kind.isInterface
        }.flatMap {
            findAnnotatedElements(it, annotationElements)
        } + when (element.kind) {
            ElementKind.METHOD, ElementKind.CONSTRUCTOR -> {
                (element as ExecutableElement).parameters.flatMap {
                    findAnnotatedElements(it, annotationElements)
                }
            }
            else -> emptyList()
        } + annotationElements.filter { annotationElement ->
            element.annotationMirrors.any { it.annotationType.asElement() == annotationElement }
        }.map {
            it to element
        }
    }

    fun getIndexes(): List<LibraryIndex> {
        return env.elementUtils.getPackageElement(PACKAGE_NAME)
            .enclosedElements
            .filterIsInstance<TypeElement>()
            .mapNotNull {
                it.getAnnotation(LibraryIndex::class.java)
            }
    }

    fun load(): Map<TypeElement, List<Element>> {
        return getIndexes().flatMap {
            it.value.flatMap { findAnnotatedElementsByTypeName(it) }
        }.fold(HashMap<TypeElement, ArrayList<Element>>()) { acc, pair ->
            acc.also { map ->
                map.getOrPut(pair.first) { ArrayList() }.add(pair.second)
            }
        }
    }

}