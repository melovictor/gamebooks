package hu.zagor.gamebooks.exception;

/**
 * Exception for marking the fact that the player tried to collect an item that cannot be gathered at the given location.
 * @author Tamas_Szekeres
 *
 */
public class InvalidGatheredItemException extends GamebookError {
    /**
     * Basic constructor that contains information about which item collection has failed where.
     * @param itemId the id of the item
     * @param paragraphId the id of the paragraph where this has happened
     */
    public InvalidGatheredItemException(final String itemId, final String paragraphId) {
        super("User tried to collect item '" + itemId + "' at paragraph '" + paragraphId + "'");
    }

}
