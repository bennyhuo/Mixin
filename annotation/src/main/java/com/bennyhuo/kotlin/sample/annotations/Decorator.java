package com.bennyhuo.kotlin.sample.annotations;

import com.bennyhuo.kotlin.decorator.runtime.IDecorator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by benny.
 */
@Repeatable(Decorators.class)
public @interface Decorator {
    
    Class<? extends IDecorator> value();
    
}
