package com.bennyhuo.kotlin.mixin.sample.library2

import com.bennyhuo.kotlin.mixin.annotations.Mixin

/**
 * Created by benny.
 */
@Mixin("com.bennyhuo.kotlin.mixin.sample", "Apis")
class Api2 {
    
    val url2 = "https://www.api2.com"
    
    fun getObject2(key: String): Any {
        return ""
    }
    
    fun getObjects2(keys: Array<String>): List<Any> {
        return emptyList()
    }
    
    fun save() {
        
    }

    fun save(k: String, v: String) {

    }

    fun save(k: String, v: Any?, options: Int = 0) {

    }
}