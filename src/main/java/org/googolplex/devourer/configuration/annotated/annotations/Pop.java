package org.googolplex.devourer.configuration.annotated.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 18.02.13
 * Time: 19:37
 *
 * @author Vladimir Matveev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Pop {
}
