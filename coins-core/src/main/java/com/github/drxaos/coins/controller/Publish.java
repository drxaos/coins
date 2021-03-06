package com.github.drxaos.coins.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Publish {
    AbstractRestPublisher.Method method();

    String path();

    boolean anonymousAccess() default false;
}
