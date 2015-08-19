package hu.zagor.gamebooks.books.saving.xml;

import hu.zagor.gamebooks.books.saving.xml.exception.UnknownFieldTypeException;

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

    /**
     * Raises a {@link UnknownFieldTypeException} for the specific class.
     * @param className the type of the field that is unknown for us
     * @throws UnknownFieldTypeException the raised exception itself
     */
    void raiseFieldException(String className) throws UnknownFieldTypeException;

}
