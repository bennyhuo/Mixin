package com.bennyhuo.kotlin.processor.module.xprocessing

import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XTypeElement
import com.bennyhuo.kotlin.processor.module.IndexLoader
import com.bennyhuo.kotlin.processor.module.LibraryIndex
import com.bennyhuo.kotlin.processor.module.utils.PACKAGE_NAME

/**
 * Created by benny.
 */
class XProcessingIndexLoader(
    private val env: XProcessingEnv
): IndexLoader<XTypeElement> {

    override fun getAnnotation(element: XTypeElement): LibraryIndex? {
        return element.getAnnotation(LibraryIndex::class)?.value
    }

    override fun getElement(name: String): XTypeElement? {
        return env.findTypeElement(name)
    }

    override fun getIndexes(): List<XTypeElement> {
        return env.getTypeElementsFromPackage(PACKAGE_NAME)
    }

}