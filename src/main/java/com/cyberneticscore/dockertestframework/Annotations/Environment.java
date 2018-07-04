package com.cyberneticscore.dockertestframework.Annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Environments.class)
public @interface Environment {
    String value();
}


