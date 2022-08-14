package com.bennyhuo.kotlin.processor.module.common

import com.bennyhuo.kotlin.processor.module.common.UniElement

/**
 * Created by benny.
 */

internal interface IndexGenerator {

    fun generate(elements: Collection<UniElement>)

}