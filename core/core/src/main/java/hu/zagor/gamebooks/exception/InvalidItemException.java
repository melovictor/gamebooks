package hu.zagor.gamebooks.exception;

/**
 * Exception for marking the fact that the player tried to collect an item that doesn't exists in the book.
 * @author Tamas_Szekeres
 *
 */
public class InvalidItemException extends GamebookError {
    /**
     * Basic constructor that contains information about which item collection has failed where.
     * @param itemId the id of the item
     */
    public InvalidItemException(final String itemId) {
        super("User tried to collect nonexistent item '" + itemId + "'!");
    }
}
