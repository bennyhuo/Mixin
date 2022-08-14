package com.bennyhuo.kotlin.processor.module.apt

import com.bennyhuo.kotlin.processor.module.common.IndexLoader
import com.bennyhuo.kotlin.processor.module.LibraryIndex
import com.bennyhuo.kotlin.processor.module.common.UniTypeElement
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

/**
 * Created by benny.
 */
internal class AptIndexLoader(
    private val env: ProcessingEnvironment,
    override val annotations: Set<String>
) : IndexLoader {
    override fun getTypeElement(typeName: String): UniTypeElement? {
        return env.elementUtils.getTypeElement(typeName)?.toUniElement()
    }

    override fun getIndexes(): List<LibraryIndex> {
        return env.elementUtils.getPackageElement(PACKAGE_NAME)
            .enclosedElements
            .filterIsInstance<TypeElement>()
            .mapNotNull {
                it.getAnnotation(LibraryIndex::class.java)
            }
    }

    fun loadUnwrap() = load().mapKeys {
        it.key.unwrapApt()
    }.mapValues {
        it.value.map { it.unwrapApt() }
    }

}