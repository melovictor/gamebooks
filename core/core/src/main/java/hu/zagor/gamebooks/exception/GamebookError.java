package hu.zagor.gamebooks.exception;

/**
 * Exception marking a problem inside the webapp.
 * @author Tamas_Szekeres
 *
 */
public class GamebookError extends RuntimeException {

    /**
     * Basic constructor with a message.
     * @param message a message describing the problem
     */
    public GamebookError(final String message) {
        super(message);
    }

}
