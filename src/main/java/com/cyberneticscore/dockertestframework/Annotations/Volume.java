package com.cyberneticscore.dockertestframework.Annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Volumes.class)
public @interface Volume {
    String local();
    String remote();
}

