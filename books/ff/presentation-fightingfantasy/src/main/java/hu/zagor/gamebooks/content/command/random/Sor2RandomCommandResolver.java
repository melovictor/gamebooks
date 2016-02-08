package hu.zagor.gamebooks.content.command.random;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.UserInteractionHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import java.util.List;
import java.util.Locale;

/**
 * Specialized random command resolver for Sorcery 2.
 * @author Tamas_Szekeres
 */
public class Sor2RandomCommandResolver extends RandomCommandResolver {
    private static final int ROLL_1 = 1;
    private static final int ROLL_2 = 2;
    private static final int ROLL_3 = 3;
    private static final int ROLL_4 = 4;
    private static final int ROLL_5 = 5;

    @Override
    public List<ParagraphData> doResolve(final RandomCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> result = super.doResolve(command, resolvationData);
        final Paragraph paragraph = resolvationData.getParagraph();
        if (("264".equals(paragraph.getId()) || "264c".equals(paragraph.getId())) && result != null) {
            final ParagraphData paragraphData = result.get(0);
            String text = paragraphData.getText();
            switch (command.getDiceResult()) {
            case ROLL_1:
                text = alternateItemExchange(resolvationData, text);
                break;
            case ROLL_2:
                text = simpleItemExchange(text, resolvationData, "3052", command);
                break;
            case ROLL_3:
                text = simpleItemExchange(text, resolvationData, "3051", command);
                break;
            case ROLL_4:
                text = simpleItemExchange(text, resolvationData, "3050", command);
                break;
            case ROLL_5:
                text = simpleItemExchange(text, resolvationData, "4040", command);
                break;
            default:
                text = doubleItemExchange(text, resolvationData);
                break;
            }
            paragraphData.setText(text);
        }

        return result;
    }

    private String alternateItemExchange(final ResolvationData resolvationData, final String text) {
        String newText = text;
        final Character character = resolvationData.getCharacter();
        final UserInteractionHandler interactionHandler = resolvationData.getInfo().getCharacterHandler().getInteractionHandler();
        final String heroItems = interactionHandler.getInteractionState(character, "gnomeHagglingOriginalItems");
        final Locale locale = getLocaleProvider().getLocale();
        final ParagraphData data = resolvationData.getParagraph().getData();
        if (heroItems.contains("////")) {
            newText = setGnomeNotInterested(locale, data);
        } else {
            for (final Item item : character.getEquipment()) {
                final ItemType itemType = item.getItemType();
                if (itemType == ItemType.weapon1 || itemType == ItemType.weapon2 || itemType == ItemType.provision || itemType == ItemType.shadow
                    || itemType == ItemType.curseSickness) {
                    continue;
                }
                if (heroItems.contains(item.getId())) {
                    continue;
                }
                data.addChoice(new Choice("264b", getMessageSource().getMessage("page.sor2.gnomeHaggling.itemChoice", new Object[]{item.getName()}, locale),
                    Integer.parseInt(item.getId()), null));
            }
            interactionHandler.setInteractionState(character, "gnomeHagglingOriginalItems", heroItems + ",////");
            if (data.getChoices().isEmpty()) {
                newText = setGnomeNotInterested(locale, data);
            }
        }
        return newText;
    }

    private String setGnomeNotInterested(final Locale locale, final ParagraphData data) {
        data.addChoice(new Choice("264d", null, 0, null));
        return getMessageSource().getMessage("page.sor2.gnomeHaggling.notInterested", null, locale);
    }

    private String doubleItemExchange(final String text, final ResolvationData resolvationData) {
        String newText;
        final Character character = resolvationData.getCharacter();
        final UserInteractionHandler interactionHandler = resolvationData.getInfo().getCharacterHandler().getInteractionHandler();
        String heroItems = interactionHandler.getInteractionState(character, "gnomeHagglingItems");
        final String[] heroItemArray = heroItems.split(",");
        if (heroItems.contains("++++")) {
            newText = getMessageSource().getMessage("page.sor2.gnomeHaggling.notInterested", null, getLocaleProvider().getLocale());
        } else {
            final DiceConfiguration configuration = new DiceConfiguration(2, 2, 5);
            int[] rolledNumbers;
            do {
                rolledNumbers = getGenerator().getRandomNumber(configuration);
            } while (rolledNumbers[1] == rolledNumbers[2] || "----".equals(heroItemArray[rolledNumbers[1] - 2]) || "----".equals(heroItemArray[rolledNumbers[2] - 2]));

            final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
            final List<Item> itemA = itemHandler.removeItem(character, heroItemArray[rolledNumbers[1] - 2], 1);
            final List<Item> itemB = itemHandler.removeItem(character, heroItemArray[rolledNumbers[2] - 2], 1);
            newText = text.replace("XXX", itemA.get(0).getName()).replace("YYY", itemB.get(0).getName());
            heroItems = heroItems.replace(itemA.get(0).getId(), "++++").replace(itemB.get(0).getId(), "++++");
            interactionHandler.setInteractionState(character, "gnomeHagglingItems", heroItems);
        }
        return newText;
    }

    private String simpleItemExchange(final String text, final ResolvationData resolvationData, final String gnomeItem, final RandomCommand command) {
        String newText;

        final Character character = resolvationData.getCharacter();
        final UserInteractionHandler interactionHandler = resolvationData.getInfo().getCharacterHandler().getInteractionHandler();
        String heroItems = interactionHandler.getInteractionState(character, "gnomeHagglingItems");
        final String[] heroItemArray = heroItems.split(",");

        final String selfItemId = heroItemArray[command.getDiceResult() - 2];

        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
        if ("----".equals(selfItemId)) {
            newText = getMessageSource().getMessage("page.sor2.gnomeHaggling.notInterested", null, getLocaleProvider().getLocale());
        } else if ("++++".equals(selfItemId)) {
            itemHandler.addItem(character, gnomeItem, 1);
            final Item addedItem = itemHandler.getItem(character, gnomeItem);
            newText = getMessageSource().getMessage("page.sor2.gnomeHaggling.freeGiveUp", new Object[]{addedItem.getName()}, getLocaleProvider().getLocale());
        } else {
            final List<Item> removedItem = itemHandler.removeItem(character, selfItemId, 1);
            itemHandler.addItem(character, gnomeItem, 1);
            final Item addedItem = itemHandler.getItem(character, gnomeItem);

            heroItems = heroItems.replace(selfItemId, "----");
            interactionHandler.setInteractionState(character, "gnomeHagglingItems", heroItems);

            newText = text.replace("XXX", addedItem.getName()).replace("YYY", removedItem.get(0).getName());
        }
        return newText;
    }

}
