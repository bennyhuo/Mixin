package com.bennyhuo.kotlin.sample.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * Created by benny.
 */
@Target(ElementType.TYPE)
public @interface Decorators {
    Decorator[] value() default {};
}
