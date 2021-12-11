package com.bennyhuo.kotlin.mixin.sample.library1

import com.bennyhuo.kotlin.mixin.annotations.Mixin

/**
 * Created by benny.
 */
@Mixin("com.bennyhuo.kotlin.mixin.sample", "Apis")
class Api1 {
    
    val url1 = "https://www.api1.com"
    
    fun getObject1(key: String): Any {
        return ""
    }
    
    fun getObjects1(keys: Array<String>): List<Any> {
        return emptyList()
    }
    
    fun save() {
        
    }
    
    fun save(key: String, value: String) {
        
    }

    fun save(key: String, value: Any?) {

    }
}