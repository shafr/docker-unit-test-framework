package io.github.shafr.dockertestframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets the location for Docker Host - including protocol, host and port.
 * <p>
 * <b>Defaults to "http://localhost:2375"</b>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DockerHost {
    String value() default "http://localhost:2375";
}
