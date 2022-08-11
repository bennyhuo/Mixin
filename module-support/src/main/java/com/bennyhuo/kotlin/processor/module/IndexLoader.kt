package com.bennyhuo.kotlin.processor.module

/**
 * Created by benny.
 */
interface IndexLoader<Element> {

    fun getAnnotation(element: Element): LibraryIndex?

    fun getElement(name: String): Element?

    fun getIndexes(): List<Element>

    fun load(): List<Element> {
        return getIndexes().mapNotNull {
            getAnnotation(it)
        }.flatMap {
            it.value.mapNotNull { getElement(it) }
        }
    }

}