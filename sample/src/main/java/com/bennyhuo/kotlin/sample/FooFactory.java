package com.bennyhuo.kotlin.sample;

public class FooFactory {

    private static final Decorators.TimeMeasureDecorator TIME_MEASURE_DECORATOR = new Decorators.TimeMeasureDecorator();
    private static final Decorators.CachedDecorator CACHED_DECORATOR = new Decorators.CachedDecorator();
    private static final Decorators.SymbolDecorator SYMBOL_DECORATOR = new Decorators.SymbolDecorator();

    public static Foo createFooImpl() {
        return CACHED_DECORATOR.decorate(
                TIME_MEASURE_DECORATOR.decorate(SYMBOL_DECORATOR.decorate(new FooImpl()))
        );
    }

}
