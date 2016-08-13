package hu.zagor.gamebooks.content;

/**
 * A runtime exception for wrapping the {@link CloneNotSupportedException}.
 * @author Tamas_Szekeres
 */
public class CloneFailedException extends RuntimeException {
    /**
     * Basic constructor that envelopes the original one.
     * @param origin the original exception
     */
    public CloneFailedException(final CloneNotSupportedException origin) {
        super(origin);
    }
}
