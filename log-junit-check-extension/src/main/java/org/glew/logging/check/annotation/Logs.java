package org.glew.logging.check.annotation;

import org.glew.logging.check.LogLinePredicate;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Repeatable(ManyLogs.class)
public @interface Logs {

    Class<?> forClass() default Logs.class;

    Class<? extends LogLinePredicate>[] matching() default {};

    String[] shouldNotContain() default {};

    String[] shouldContain() default {};
}
