package hu.zagor.gamebooks.ff.mvc.book.inventory.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * {@link MarketHandler} implementation for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
@Component
public class FfMarketHandler implements MarketHandler {

    @Override
    public Map<String, Object> handleMarketPurchase(final String itemId, final Character characterObject, final ParagraphData data, final CharacterItemHandler itemHandler) {
        final FfCharacter character = (FfCharacter) characterObject;

        final MarketCommand command = (MarketCommand) data.getCommands().get(0);
        final List<MarketElement> itemsForSale = command.getItemsForSale();
        final MarketElement toBuy = fetchItemFromList(itemId, itemsForSale);
        final Map<String, Object> result = new HashMap<>();
        result.put("successfulTransaction", false);
        if (toBuy != null) {
            final int gold = character.getGold();
            if (gold >= toBuy.getPrice() && toBuy.getStock() > 0) {
                character.setGold(gold - toBuy.getPrice());
                itemHandler.addItem(character, toBuy.getId(), 1);
                toBuy.setStock(toBuy.getStock() - 1);
                result.put("successfulTransaction", true);
            }
        }
        final int gold = character.getGold();
        result.put("gold", gold);
        return result;
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
    public Map<String, Object> handleMarketSell(final String itemId, final Character characterObject, final ParagraphData data, final CharacterItemHandler itemHandler) {
        final FfCharacter character = (FfCharacter) characterObject;

        final MarketCommand command = (MarketCommand) data.getCommands().get(0);
        final List<MarketElement> itemsForPurchase = command.getItemsForPurchase();
        final MarketElement toSell = fetchItemFromList(itemId, itemsForPurchase);
        final Map<String, Object> result = new HashMap<>();

        result.put("successfulTransaction", false);

        if (toSell != null) {
            if (itemHandler.hasItem(character, toSell.getId())) {
                character.setGold(character.getGold() + toSell.getPrice());
                itemHandler.removeItem(character, toSell.getId(), 1);
                toSell.setStock(toSell.getStock() - 1);
                result.put("successfulTransaction", true);

                if (command.isGiveUpMode()) {
                    command.setGiveUpAmount(command.getGiveUpAmount() - 1);
                }
            }
        }

        result.put("gold", character.getGold());
        result.put("giveUpMode", command.isGiveUpMode());
        result.put("giveUpFinished", command.getGiveUpAmount() == 0);

        return result;
    }

}
