package hu.zagor.gamebooks.books.saving.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks those fields which can be ignored by the reader for a specific class.
 * @author Tamas_Szekeres
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IgnoreField {

    /**
     * The list of field names which can be safely ignored.
     * @return the list of field names
     */
    String value();

}
