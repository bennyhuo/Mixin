package com.bennyhuo.kotlin.processor.module.ksp

import com.bennyhuo.kotlin.processor.module.common.IndexLoader
import com.bennyhuo.kotlin.processor.module.LibraryIndex
import com.bennyhuo.kotlin.processor.module.common.UniElement
import com.bennyhuo.kotlin.processor.module.common.UniTypeElement
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 * Created by benny.
 */
internal class KspIndexLoader(
    private val resolver: Resolver,
    override val annotations: Set<String>
) : IndexLoader {

    override fun findAnnotatedElementsByTypeName(enclosingTypeName: String): Collection<Pair<UniTypeElement, UniElement>> {
        val annotationElements = annotations.mapNotNull { getTypeElement(it) }
        val declarationName = DeclarationName.parse(enclosingTypeName)

        return resolver.getDeclarations(declarationName).flatMap {
            findAnnotatedElements(it.toUniElement(), annotationElements)
        }
    }

    override fun getTypeElement(typeName: String): UniTypeElement? {
        return resolver.getClassDeclarationByName(typeName)?.toUniElement()
    }

    override fun getIndexes(): List<LibraryIndex> {
        return resolver.getDeclarationsFromPackage(PACKAGE_NAME)
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull {
                it.getAnnotationsByType(LibraryIndex::class).firstOrNull()
            }.toList()
    }

    fun loadUnwrapped() = load().mapKeys {
        it.key.unwrapKsp()
    }.mapValues {
        it.value.map { it.unwrapKsp() }
    }
}