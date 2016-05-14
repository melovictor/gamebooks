package hu.zagor.gamebooks.ff.mvc.book.inventory.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * {@link MarketHandler} implementation for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
@Component
public class FfMarketHandler implements MarketHandler {

    @Override
    public BuySellResponse handleMarketPurchase(final String itemId, final Character characterObject, final Command commandObject,
        final CharacterHandler characterHandler) {
        final FfCharacter character = (FfCharacter) characterObject;

        final MarketCommand command = (MarketCommand) commandObject;
        final List<MarketElement> itemsForSale = command.getItemsForSale();
        final MarketElement toBuy = fetchItemFromList(itemId, itemsForSale);
        final BuySellResponse result = new BuySellResponse();
        result.setSuccessfulTransaction(false);
        final FfCharacterHandler handler = (FfCharacterHandler) characterHandler;
        final FfAttributeHandler attributeHandler = handler.getAttributeHandler();
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

    private int getCurrentGoldAmount(final FfCharacter character, final MarketCommand command, final FfAttributeHandler attributeHandler) {
        int resolveValue;
        if ("gold".equals(command.getMoneyAttribute())) {
            resolveValue = character.getGold();
        } else {
            resolveValue = attributeHandler.resolveValue(character, command.getMoneyAttribute());
        }
        return resolveValue;
    }

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
    public BuySellResponse handleMarketSell(final String itemId, final Character characterObject, final Command commandObject, final CharacterHandler characterHandler) {
        final FfCharacter character = (FfCharacter) characterObject;

        final MarketCommand command = (MarketCommand) commandObject;
        final List<MarketElement> itemsForPurchase = command.getItemsForPurchase();
        final MarketElement toSell = fetchItemFromList(itemId, itemsForPurchase);
        final BuySellResponse result = new BuySellResponse();

        result.setSuccessfulTransaction(false);

        final FfCharacterHandler handler = (FfCharacterHandler) characterHandler;
        final FfAttributeHandler attributeHandler = handler.getAttributeHandler();

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

}
