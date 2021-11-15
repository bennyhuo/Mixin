package com.bennyhuo.kotlin.sample;

/**
 * Created by benny.
 */
public class Main {
    public static void main(String[] args) {
        Foo foo = FooFactory.createFooImpl();
        System.out.println(foo.bar());
        System.out.println(foo.bar());
        System.out.println(foo.bar());
        System.out.println(foo.bar());
        System.out.println(foo.bar());
    }
}
