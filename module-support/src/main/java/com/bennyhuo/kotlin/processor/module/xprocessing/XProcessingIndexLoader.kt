package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XExecutableElement
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.isTypeElement
import com.bennyhuo.kotlin.processor.module.LibraryIndex
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME

/**
 * Created by benny.
 */
class XProcessingIndexLoader(
    private val env: XProcessingEnv,
    val annotations: Set<String>
) {

    fun findAnnotatedElementsByTypeName(enclosingTypeName: String): Collection<Pair<String, XElement>> {
        val enclosingTypeElement = env.findTypeElement(enclosingTypeName) ?: return emptyList()
        val annotationElements = annotations.mapNotNull { env.findTypeElement(it) }

        return findAnnotatedElements(enclosingTypeElement, annotationElements)
    }

    private fun findAnnotatedElements(
        element: XElement,
        annotationElements: Collection<XTypeElement>
    ): Collection<Pair<String, XElement>> {
        return ((element as? XTypeElement)?.getEnclosedElements()?.filter {
            // TypeElements are already found.
            !it.isTypeElement()
        }?.flatMap {
            findAnnotatedElements(it, annotationElements)
        } ?: emptyList()) + ((element as? XExecutableElement)?.parameters?.flatMap {
            findAnnotatedElements(it, annotationElements)
        } ?: emptyList()) + annotationElements.filter { annotationElement ->
            element.getAllAnnotations().any { it.type.isSameType(annotationElement.type) }
        }.map {
            it.qualifiedName to element
        }
    }

    fun getIndexes(): List<LibraryIndex> {
        return env.getTypeElementsFromPackage(PACKAGE_NAME)
            .mapNotNull {
                it.getAnnotation(LibraryIndex::class)?.value
            }
    }

    fun load(): Map<String, List<XElement>> {
        return getIndexes().flatMap {
            it.value.flatMap { findAnnotatedElementsByTypeName(it) }
        }.fold(HashMap<String, ArrayList<XElement>>()) { acc, pair ->
            acc.also { map ->
                map.getOrPut(pair.first) { ArrayList() }.add(pair.second)
            }
        }
    }

}