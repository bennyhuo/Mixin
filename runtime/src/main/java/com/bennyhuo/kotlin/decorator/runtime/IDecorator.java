package com.bennyhuo.kotlin.decorator.runtime;

public interface IDecorator<T> {

    T decorate(T t);

}
