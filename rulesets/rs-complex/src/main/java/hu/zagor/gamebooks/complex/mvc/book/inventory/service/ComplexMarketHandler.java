package hu.zagor.gamebooks.complex.mvc.book.inventory.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.ComplexAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import java.util.List;

/**
 * Common {@link MarketHandler} implementation.
 * @author Tamas_Szekeres
 * @param <C> the actual {@link Character} type
 */
public abstract class ComplexMarketHandler<C extends Character> implements MarketHandler<C> {

    @Override
    public BuySellResponse handleMarketPurchase(final String itemId, final C character, final MarketCommand command, final CharacterHandler characterHandler) {
        final List<MarketElement> itemsForSale = command.getItemsForSale();
        final MarketElement toBuy = fetchItemFromList(itemId, itemsForSale);
        final BuySellResponse result = new BuySellResponse();
        result.setSuccessfulTransaction(false);
        final CharacterHandler handler = characterHandler;
        final ComplexAttributeHandler<C> attributeHandler = getAttributeHandler(handler);
        if (toBuy != null) {
            final int gold = getCurrentGoldAmount(character, command, attributeHandler);
            if (gold >= toBuy.getPrice() && toBuy.getStock() > 0) {
                attributeHandler.handleModification(character, command.getMoneyAttribute(), -toBuy.getPrice());
                final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
                itemHandler.addItem(character, toBuy.getId(), 1);
                toBuy.setStock(toBuy.getStock() - 1);
                result.setSuccessfulTransaction(true);
            }
        }

        result.setGold(getCurrentGoldAmount(character, command, attributeHandler));
        return result;
    }

    private int getCurrentGoldAmount(final C character, final MarketCommand command, final ComplexAttributeHandler<C> attributeHandler) {
        int resolveValue;
        if ("gold".equals(command.getMoneyAttribute())) {
            resolveValue = getGold(character);
        } else {
            resolveValue = attributeHandler.resolveValue(character, command.getMoneyAttribute());
        }
        return resolveValue;
    }

    /**
     * Returns the default gold amount for the {@link Character} object.
     * @param character the {@link Character} to return the gold amount from
     * @return the gold amount
     */
    protected abstract int getGold(C character);

    private MarketElement fetchItemFromList(final String itemId, final List<MarketElement> list) {
        MarketElement toBuy = null;
        for (final MarketElement item : list) {
            if (itemId.equals(item.getId())) {
                toBuy = item;
            }
        }
        return toBuy;
    }

    @Override
    public BuySellResponse handleMarketSell(final String itemId, final C character, final MarketCommand command, final CharacterHandler characterHandler) {
        final List<MarketElement> itemsForPurchase = command.getItemsForPurchase();
        final MarketElement toSell = fetchItemFromList(itemId, itemsForPurchase);
        final BuySellResponse result = new BuySellResponse();

        result.setSuccessfulTransaction(false);

        final CharacterHandler handler = characterHandler;
        final ComplexAttributeHandler<C> attributeHandler = getAttributeHandler(handler);

        if (toSell != null) {
            final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
            if (itemHandler.hasItem(character, toSell.getId())) {
                attributeHandler.handleModification(character, command.getMoneyAttribute(), toSell.getPrice());

                itemHandler.removeItem(character, toSell.getId(), 1);
                toSell.setStock(toSell.getStock() - 1);
                result.setSuccessfulTransaction(true);

                if (command.getGiveUpMode() != null) {
                    command.setGiveUpAmount(command.getGiveUpAmount() - 1);
                }
            }
        }

        result.setGold(getCurrentGoldAmount(character, command, attributeHandler));
        result.setGiveUpMode(command.getGiveUpMode() != null);
        result.setGiveUpFinished(command.getGiveUpAmount() == 0);

        return result;
    }

    @SuppressWarnings("unchecked")
    private ComplexAttributeHandler<C> getAttributeHandler(final CharacterHandler handler) {
        return (ComplexAttributeHandler<C>) handler.getAttributeHandler();
    }

}
