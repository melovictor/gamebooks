package hu.zagor.gamebooks.mvc.book.inventory.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;

/**
 * Interface for handling the market transactions.
 * @author Tamas_Szekeres
 * @param <C> the actual {@link Character} type
 */
public interface MarketHandler<C extends Character> {

    /**
     * Handles the purchase of an item from the market.
     * @param itemId the id of the item
     * @param character the {@link Character}
     * @param command the {@link MarketCommand}
     * @param characterHandler the {@link CharacterHandler}
     * @return the response containing the result of the purchase
     */
    BuySellResponse handleMarketPurchase(String itemId, C character, MarketCommand command, CharacterHandler characterHandler);

    /**
     * Handles the selling of an item to the market.
     * @param itemId the id of the item
     * @param character the {@link Character}
     * @param command the {@link MarketCommand}
     * @param characterHandler the {@link CharacterHandler}
     * @return the response containing the result of the selling
     */
    BuySellResponse handleMarketSell(String itemId, C character, MarketCommand command, CharacterHandler characterHandler);

}
