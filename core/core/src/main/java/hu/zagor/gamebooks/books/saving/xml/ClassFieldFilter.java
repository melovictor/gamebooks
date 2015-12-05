package hu.zagor.gamebooks.books.saving.xml;

import java.lang.reflect.Field;

/**
 * Interface for outsourcing some {@link Field}-related operations.
 * @author Tamas_Szekeres
 */
public interface ClassFieldFilter {

    /**
     * Determiens wherther a specific field in a specific object is ignorable or not.
     * @param instance the target parent instance
     * @param nodeName the name of the field
     * @return true if the field is not required, false if it is
     */
    boolean isIgnorableField(Object instance, String nodeName);

}
