package hu.zagor.gamebooks.books.saving.xml.exception;

/**
 * Exception to throw when an unexpected primitive type is encountered during unmarshalling.
 * @author Tamas_Szekeres
 */
public class UnknownFieldTypeException extends Exception {

    private final String className;

    /**
     * Basic constructor with the problematic class name.
     * @param className the name of the class that couldn't be unmarshalled.
     */
    public UnknownFieldTypeException(final String className) {
        this.className = className;
    }

    @Override
    public String getMessage() {
        return "Couldn't unmarshall type '" + className + "'.";
    }
}
