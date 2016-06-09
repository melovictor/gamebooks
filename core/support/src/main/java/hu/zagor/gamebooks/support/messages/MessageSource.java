package hu.zagor.gamebooks.support.messages;

/**
 * Interface for resolving a text key without specifying a locale, which will have to be resolved dynamically from somewhere.
 * @author Tamas_Szekeres
 */
public interface MessageSource extends org.springframework.context.MessageSource {

    /**
     * Resolve the specified code using the provided arguments.
     * @param code the code to resolve
     * @param args the arguments
     * @return the resolved text in the proper language
     */
    String getMessage(String code, Object... args);

    /**
     * Resolve the specified code without arguments.
     * @param code the code to resolve
     * @return the resolved text in the proper language
     */
    String getMessage(String code);

}
