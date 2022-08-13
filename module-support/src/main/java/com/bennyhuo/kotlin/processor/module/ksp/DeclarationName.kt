package com.bennyhuo.kotlin.processor.module.ksp

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.getFunctionDeclarationsByName
import com.google.devtools.ksp.getPropertyDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSDeclaration

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

fun Resolver.getDeclarations(declarationName: DeclarationName): Collection<KSDeclaration> {
    return when (declarationName.type) {
        DECLARATION_CLASS -> {
            getClassDeclarationByName(declarationName.name)?.let { listOf(it) } ?: emptyList()
        }
        DECLARATION_FUNCTION -> getFunctionDeclarationsByName(declarationName.name, true).toList()
        DECLARATION_PROPERTY -> {
            getPropertyDeclarationByName(declarationName.name, true)?.let { listOf(it) } ?: emptyList()
        }
        else -> throw IllegalArgumentException()
    }
}