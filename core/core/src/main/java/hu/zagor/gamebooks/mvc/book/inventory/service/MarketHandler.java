package hu.zagor.gamebooks.mvc.book.inventory.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.ParagraphData;

import java.util.Map;

/**
 * Interface for handling the market transactions.
 * @author Tamas_Szekeres
 */
public interface MarketHandler {

    /**
     * Handles the purchase of an item from the market.
     * @param itemId the id of the item
     * @param character the {@link Character}
     * @param data the {@link ParagraphData}
     * @param itemHandler the {@link CharacterItemHandler}
     * @return the map containing the result of the purchase
     */
    Map<String, Object> handleMarketPurchase(String itemId, Character character, ParagraphData data, CharacterItemHandler itemHandler);

    /**
     * Handles the selling of an item to the market.
     * @param itemId the id of the item
     * @param character the {@link Character}
     * @param data the {@link ParagraphData}
     * @param itemHandler the {@link CharacterItemHandler}
     * @return the map containing the result of the selling
     */
    Map<String, Object> handleMarketSell(String itemId, Character character, ParagraphData data, CharacterItemHandler itemHandler);

}
