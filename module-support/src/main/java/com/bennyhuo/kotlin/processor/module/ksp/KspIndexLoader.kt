package com.bennyhuo.kotlin.processor.module.ksp

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XTypeElement
import com.bennyhuo.kotlin.processor.module.LibraryIndex
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSDeclarationContainer
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

/**
 * Created by benny.
 */
class KspIndexLoader(
    private val resolver: Resolver,
    val annotations: Set<String>
) {

    fun findAnnotatedElementsByTypeName(enclosingTypeName: String): Collection<Pair<KSClassDeclaration, KSAnnotated>> {
        val enclosingTypeElement = resolver.getClassDeclarationByName(enclosingTypeName)!!
        val annotationElements = annotations.mapNotNull { resolver.getClassDeclarationByName(it) }

        return findAnnotatedElements(enclosingTypeElement, annotationElements)
    }

    private fun findAnnotatedElements(
        element: KSAnnotated,
        annotationElements: Collection<KSClassDeclaration>
    ): Collection<Pair<KSClassDeclaration, KSAnnotated>> {
        return ((element as? KSDeclarationContainer)?.declarations?.filter {
            // TypeElements are already found.
            it !is KSClassDeclaration
        }?.flatMap {
            findAnnotatedElements(it, annotationElements)
        }?.toList() ?: emptyList()) + ((element as? KSFunctionDeclaration)?.parameters?.flatMap {
            findAnnotatedElements(it, annotationElements)
        } ?: emptyList()) + annotationElements.filter { annotationElement ->
            element.annotations.any { it.annotationType.resolve().declaration == annotationElement }
        }.map {
            it to element
        }
    }

    fun getIndexes(): List<LibraryIndex> {
        return resolver.getDeclarationsFromPackage(PACKAGE_NAME)
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull {
                it.getAnnotationsByType(LibraryIndex::class).firstOrNull()
            }.toList()
    }

    fun load(): Map<KSClassDeclaration, List<KSAnnotated>> {
        return getIndexes().flatMap {
            it.value.flatMap { findAnnotatedElementsByTypeName(it) }
        }.fold(HashMap<KSClassDeclaration, ArrayList<KSAnnotated>>()) { acc, pair ->
            acc.also { map ->
                map.getOrPut(pair.first) { ArrayList() }.add(pair.second)
            }
        }
    }

}