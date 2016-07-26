package hu.zagor.gamebooks.content.command.market;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.ComplexUserInteractionHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Resolver class for "market" elements.
 * @author Tamas_Szekeres
 */
public abstract class ComplexMarketCommandResolver extends TypeAwareCommandResolver<MarketCommand> {

    @Override
    protected List<ParagraphData> doResolve(final MarketCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> data;

        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final Character character = resolvationData.getCharacter();
        final ComplexUserInteractionHandler interactionHandler = (ComplexUserInteractionHandler) characterHandler.getInteractionHandler();
        if (interactionHandler.hasMarketCommand(character) && hasEnoughGold(characterHandler, character, command)) {
            data = new ArrayList<>();
            data.add(command.getAfter());
        } else {
            final int forPurchase = initializeItems(command.getItemsForPurchase(), resolvationData.getCharacter().getEquipment());
            initializeItems(command.getItemsForSale(), characterHandler.getItemHandler());
            if (notEnoughItemsForForcedGet(command, forPurchase)) {
                data = new ArrayList<ParagraphData>();
                data.add(command.getEmptyHanded());
                command.setGiveUpUnsuccessful(true);
            } else {
                data = null;
            }
        }

        return data;
    }

    private boolean notEnoughItemsForForcedGet(final MarketCommand command, final int forPurchase) {
        return (command.getGiveUpMode() == GiveUpMode.asMuchAsPossible && forPurchase == 0)
            || (command.getGiveUpMode() == GiveUpMode.allOrNothing && forPurchase < command.getGiveUpAmount());
    }

    private boolean hasEnoughGold(final CharacterHandler characterHandler, final Character character, final MarketCommand command) {
        return getCurrentGoldAmount(characterHandler, character, command) >= command.getMustHaveGold();
    }

    private int getCurrentGoldAmount(final CharacterHandler characterHandler, final Character character, final MarketCommand command) {
        int resolveValue;
        if ("gold".equals(command.getMoneyAttribute())) {
            resolveValue = getDefaultGoldAmount(character);
        } else {
            resolveValue = characterHandler.getAttributeHandler().resolveValue(character, command.getMoneyAttribute());
        }
        return resolveValue;
    }

    /**
     * Returns the amount of gold based on the character's default gold storage.
     * @param character the {@link Character} whose gold we want to be returned
     * @return the amount of available gold
     */
    protected abstract int getDefaultGoldAmount(Character character);

    private int initializeItems(final List<MarketElement> elements, final List<Item> equipment) {
        int totalItems = 0;
        for (final MarketElement element : elements) {
            final int total = lookUpItems(equipment, element);
            final int available = Math.min(total, element.getStock());
            element.setStock(available);
            totalItems += available;
        }
        return totalItems;
    }

    private int lookUpItems(final List<Item> equipment, final MarketElement element) {
        int total = 0;
        final String marketElementId = element.getId();

        for (final Item item : equipment) {
            if (marketElementId.equals(item.getId())) {
                element.setName(item.getName());
                total += item.getAmount();
            }
        }

        return total;
    }

    private void initializeItems(final List<MarketElement> elements, final CharacterItemHandler itemHandler) {
        for (final MarketElement element : elements) {
            element.setName(itemHandler.resolveItem(element.getId()).getName());
        }
    }

}
