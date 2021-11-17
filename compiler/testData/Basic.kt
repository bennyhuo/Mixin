// SOURCE
// Foo.java
package com.bennyhuo.kotlin.sample;

public interface Foo {
    String bar();
}
// Decorators.java
package com.bennyhuo.kotlin.sample;

import com.bennyhuo.kotlin.decorator.runtime.IDecorator;

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
// FooImpl.java
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

// GENERATED
