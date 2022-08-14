package com.bennyhuo.kotlin.processor.module.ksp

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter

/**
 * Created by benny.
 */
internal fun KSAnnotated.getDeclarationName(): DeclarationName {
    return when (this) {
        is KSClassDeclaration -> DeclarationName(qualifiedName!!.asString(), DECLARATION_CLASS)
        is KSFunctionDeclaration -> DeclarationName(qualifiedName!!.asString(), DECLARATION_FUNCTION)
        is KSPropertyDeclaration -> DeclarationName(qualifiedName!!.asString(), DECLARATION_PROPERTY)
        is KSValueParameter -> (this.parent as KSAnnotated).getDeclarationName()
        is KSDeclaration -> {
            DeclarationName(closestClassDeclaration()!!.qualifiedName!!.asString(), DECLARATION_CLASS)
        }
        else -> {
            throw IllegalArgumentException()
        }
    }
}