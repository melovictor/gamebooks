package hu.zagor.gamebooks.recording;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;

/**
 * Interface for recording item interactions of a character throughout the adventure for later use in the automated tests.
 * @author Tamas_Szekeres
 */
public interface ItemInteractionRecorder {

    /**
     * Records the taking of an item by the character.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param itemId the itemId
     */
    void recordItemTaking(HttpSessionWrapper wrapper, String itemId);

    /**
     * Records the consumption of an item by the character.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param itemId the itemId
     */
    void recordItemConsumption(HttpSessionWrapper wrapper, String itemId);

    /**
     * Records the equipped state change of an item by the character.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param itemId the id of the item
     */
    void changeItemEquipState(HttpSessionWrapper wrapper, String itemId);

    /**
     * Records the purchase or sale of an item in the market.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param transactionType the type of the transaction
     * @param itemId the id of the item
     */
    void recordItemMarketMovement(HttpSessionWrapper wrapper, String transactionType, String itemId);

}
