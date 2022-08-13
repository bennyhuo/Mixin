package com.bennyhuo.kotlin.processor.module

import javax.lang.model.element.TypeElement

/**
 * Created by benny.
 */
interface IndexLoader<Element> {

    val annotations: Set<String>

    fun getElements(enclosingTypeName: String): Map<TypeElement, Collection<javax.lang.model.element.Element>>

    fun getIndexes(): List<LibraryIndex>



}