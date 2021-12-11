package com.bennyhuo.kotlin.mixin.sample.library1

import com.bennyhuo.kotlin.mixin.annotations.Mixin

/**
 * Created by benny.
 */
@Mixin("com.bennyhuo.kotlin.mixin.sample", "UserApis")
class UserApi {
    fun getName(id: Long): String = TODO()
    
    fun getAge(id: Long): Int = TODO()
}