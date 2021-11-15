package com.bennyhuo.kotlin.sample;

import com.bennyhuo.kotlin.decorator.runtime.IDecorator;
import com.bennyhuo.kotlin.sample.annotations.Decorator;

public class Decorators {
    
    public static class SymbolDecorator implements IDecorator<Foo> {

        @Override
        public Foo decorate(Foo foo) {
            return () -> "<<<" + foo.bar() + ">>>";
        }
    }
    
    public static class TimeMeasureDecorator implements IDecorator<Foo> {

        @Override
        public Foo decorate(Foo foo) {
            return () -> {
                long start = System.currentTimeMillis();
                String result = "<<<" + foo.bar() + ">>>";
                System.out.println("Cost: " + (System.currentTimeMillis() - start));
                return result;
            };
        }
    }
    
    public static class CachedDecorator implements IDecorator<Foo> {
        
        private String cachedValue;

        @Override
        public Foo decorate(Foo foo) {
            return () -> {
                if (cachedValue == null) {
                    cachedValue = foo.bar();
                }
                return cachedValue;
            };
        }
    }

}