package hu.zagor.gamebooks.support.mock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Instance {
    Class<?> type() default Object.class;

    boolean inject() default false;
}
