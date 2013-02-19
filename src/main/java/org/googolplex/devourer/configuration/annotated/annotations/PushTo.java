package org.googolplex.devourer.configuration.annotated.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 18.02.13
 * Time: 20:15
 *
 * @author Vladimir Matveev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PushTo {
    String value();
}
