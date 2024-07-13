package org.hbrs.se2.project.hellocar.util.builder;

public interface Builder<T> {
    T build();
    void reset();
    Builder<T> setFrom(T instance);
}