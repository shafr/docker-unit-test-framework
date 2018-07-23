package io.github.shafr.dockertestframework.annotations;

import java.lang.annotation.*;

/**
 * Set environment property for containers in class of for particular class.
 * <p> For example:
 * <pre>
 *  {@literal @}Environment("key=value")
 * </pre>
 * Can be used multiple times on one class / method.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Environments.class)
public @interface Environment {
    String value();
}


