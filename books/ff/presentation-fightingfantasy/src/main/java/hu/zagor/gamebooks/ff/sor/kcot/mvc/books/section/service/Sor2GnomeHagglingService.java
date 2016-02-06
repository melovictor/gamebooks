package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.service;

import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Service for handling tasks connected to the haggling with the Gnome.
 * @author Tamas_Szekeres
 */
@Component
public class Sor2GnomeHagglingService {
    private static final int MINIMAL_AMOUNT_OF_ITEMS_FOR_GNOME_SPELLING = 1;
    private static final int MINIMAL_AMOUNT_OF_ITEMS_FOR_GNOME_HAGGLING = 4;

    /**
     * Sets the entry condition to either true or false based on our caste and the number of items we carry.
     * @param paragraph the {@link Paragraph} object
     * @param character the {@link SorCharacter} object
     */
    public void setEntryCondition(final Paragraph paragraph, final SorCharacter character) {
        final ItemCheckCommand command = (ItemCheckCommand) paragraph.getData().getCommands().get(0);
        command.setId(hasEnoughItems(character));
    }

    private String hasEnoughItems(final SorCharacter character) {
        final boolean isWizard = character.isWizard();
        boolean hasEnoughItems;
        if (isWizard) {
            hasEnoughItems = getItemCountForGnomeHaggling(character, true) >= MINIMAL_AMOUNT_OF_ITEMS_FOR_GNOME_SPELLING;
        } else {
            hasEnoughItems = getItemCountForGnomeHaggling(character, false) >= MINIMAL_AMOUNT_OF_ITEMS_FOR_GNOME_HAGGLING;
        }
        return String.valueOf(hasEnoughItems);
    }

    private int getItemCountForGnomeHaggling(final SorCharacter character, final boolean mealsCount) {
        int total = 0;
        final Set<String> countedIds = new HashSet<>();
        for (final Item item : character.getEquipment()) {
            final ItemType itemType = item.getItemType();
            if (itemType == ItemType.weapon1 || itemType == ItemType.weapon2 || itemType == ItemType.shadow || itemType == ItemType.curseSickness) {
                continue;
            }
            final String id = item.getId();
            if (itemType == ItemType.provision) {
                if (!mealsCount) {
                    continue;
                } else if (!"2000".equals(id)) {
                    continue;
                }
            }
            if (!countedIds.contains(id)) {
                total++;
                countedIds.add(id);
            }
        }
        return total;
    }

    /**
     * Sets the entry condition to either true or false based on the number of items we carry.
     * @param paragraph the {@link Paragraph} object
     * @param character the {@link SorCharacter} object
     */
    public void setHagglingCondition(final Paragraph paragraph, final SorCharacter character) {
        final ItemCheckCommand command = (ItemCheckCommand) paragraph.getData().getCommands().get(0);
        final boolean hasEnoughItems = getItemCountForGnomeHaggling(character, false) >= MINIMAL_AMOUNT_OF_ITEMS_FOR_GNOME_HAGGLING;
        command.setId(String.valueOf(hasEnoughItems));
    }

}
