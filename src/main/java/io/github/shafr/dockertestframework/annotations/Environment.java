package io.github.shafr.dockertestframework.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Environments.class)
public @interface Environment {
    String value();
}


