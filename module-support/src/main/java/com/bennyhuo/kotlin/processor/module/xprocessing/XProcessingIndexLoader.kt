package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XProcessingEnv
import com.bennyhuo.kotlin.processor.module.common.IndexLoader
import com.bennyhuo.kotlin.processor.module.LibraryIndex
import com.bennyhuo.kotlin.processor.module.common.UniTypeElement
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME

/**
 * Created by benny.
 */
internal class XProcessingIndexLoader(
    private val env: XProcessingEnv,
    override val annotations: Set<String>
) : IndexLoader {
    override fun getTypeElement(typeName: String): UniTypeElement? {
        return env.findTypeElement(typeName)?.toUniElement()
    }

    override fun getIndexes(): List<LibraryIndex> {
        return env.getTypeElementsFromPackage(PACKAGE_NAME)
            .mapNotNull {
                it.getAnnotation(LibraryIndex::class)?.value
            }
    }

    fun loadUnwrap() = load().mapKeys { it.key.unwrapXProcessing() }
        .mapValues { it.value.map { it.unwrapXProcessing() } }
}