package com.github.drxaos.coins.application.factory;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Autowire {
    String value() default "";
}
