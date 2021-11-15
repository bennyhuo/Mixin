package com.bennyhuo.kotlin.sample;

import com.bennyhuo.kotlin.sample.annotations.Decorator;

@Decorator
public class FooDecorator implements IDecorator<Foo> {

    @Override
    public Foo decorate(Foo foo) {
        return () -> "<<<" + foo.bar() + ">>>";
    }
}
