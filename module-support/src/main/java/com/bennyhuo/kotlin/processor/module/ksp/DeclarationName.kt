package com.bennyhuo.kotlin.processor.module.ksp

/**
 * Created by benny.
 */
const val DECLARATION_CLASS = 0
const val DECLARATION_FUNCTION = 1
const val DECLARATION_PROPERTY = 2

class DeclarationName(val name: String, val type: Int) {
    companion object {
        fun parse(value: String): DeclarationName {
            val splits = value.split("|")
            val name = splits[0]
            val type = splits[1].toInt()
            return DeclarationName(name, type)
        }
    }

    override fun toString(): String {
        return "$name|$type"
    }
}