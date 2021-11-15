package com.bennyhuo.kotlin.sample;

public class FooFactory {

    private static final FooDecorator DECORATOR = new FooDecorator();

    public static Foo createFooImpl() {
        return DECORATOR.decorate(new FooImpl());
    }

}
