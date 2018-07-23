package io.github.shafr.dockertestframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stores comma-separated array - command to execute and it's arguments
 * <p> For example:
 * <pre>
 *  {@literal @}CommandLineArgument({"ls", "-ltrh"})
 * </pre>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandLineArgument {
    String[] value();
}
