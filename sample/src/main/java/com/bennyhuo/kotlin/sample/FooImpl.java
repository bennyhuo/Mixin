package com.bennyhuo.kotlin.sample;

import com.bennyhuo.kotlin.sample.annotations.Decorate;
import com.bennyhuo.kotlin.sample.annotations.Decorator;

@Decorate
public class FooImpl implements Foo {
    @Override
    public String bar() {
        return "Hello World";
    }
}
