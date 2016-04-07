package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.ParagraphData;
import java.util.List;

/**
 * Class for resolving a total items-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckTotalItemsCommandResolver implements ItemCheckStubCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        ParagraphData toResolve;
        final List<Item> equipment = resolvationData.getCharacter().getEquipment();
        final int totalItems = countActualItems(equipment);
        final boolean hasItem = totalItems >= Integer.valueOf(parent.getId());

        if (hasItem) {
            toResolve = parent.getHave();
        } else {
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }

    private int countActualItems(final List<Item> equipment) {
        int count = 0;

        for (final Item item : equipment) {
            switch (item.getItemType()) {
            case shadow:
            case curseSickness:
            case info:
                continue;
            default:
                if (!"defWpn".equals(item.getId())) {
                    count++;
                }
            }
        }

        return count;
    }

}
