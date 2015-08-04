package hu.zagor.gamebooks.exception;

/**
 * Exception marking a problem inside the webapp.
 * @author Tamas_Szekeres
 *
 */
public class GamebookException extends Exception {

    /**
     * Basic constructor with a message.
     * @param message a message describing the problem
     */
    public GamebookException(final String message) {
        super(message);
    }

}
