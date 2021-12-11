package com.bennyhuo.kotlin.mixin.sample.library2;

import com.bennyhuo.kotlin.mixin.annotations.Mixin;

import kotlin.NotImplementedError;

/**
 * Created by benny.
 */
@Mixin(packageName = "com.bennyhuo.kotlin.mixin.sample", name = "UserApis")
public class GitHubUserApi {
    public String getGitHubUrl(long id) {
        throw new NotImplementedError(); 
    }
    
    public int getRepositoryCount(long id) {
        throw new NotImplementedError(); 
    }
}
