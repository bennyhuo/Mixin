package com.bennyhuo.kotlin.sample;

import com.bennyhuo.kotlin.sample.annotations.Decorator;

@Decorator(Decorators.CachedDecorator.class)
@Decorator(Decorators.TimeMeasureDecorator.class)
@Decorator(Decorators.SymbolDecorator.class)
public class FooImpl implements Foo {
    @Override
    public String bar() {
        return "Hello World";
    }
}

