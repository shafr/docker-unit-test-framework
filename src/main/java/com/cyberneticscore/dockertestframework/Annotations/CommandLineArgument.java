package com.cyberneticscore.dockertestframework.Annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandLineArgument {
    String command() default "";
    String[] arguments() default "";
}
