package com.bennyhuo.kotlin.mixin.compiler

import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XTypeElement
import com.bennyhuo.kotlin.mixin.annotations.MixinIndex
import javax.tools.Diagnostic

/**
 * Created by benny.
 */
class MixinIndexLoader {

    fun loadIndex(env: XProcessingEnv): List<XTypeElement> {
        return env.getTypeElementsFromPackage(MixinIndexGenerator.INDEX_PACKAGE)
            .mapNotNull { it.getAnnotation(MixinIndex::class) }
            .flatMap {
                it.value.value.mapNotNull {
                    env.findTypeElement(it)
                }
            }
    }

}