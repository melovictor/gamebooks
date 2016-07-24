package hu.zagor.gamebooks.lw.item;

import hu.zagor.gamebooks.books.contentransforming.item.AbstractBookItemTransformer;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.character.item.LwItem;
import hu.zagor.gamebooks.character.item.Placement;
import org.w3c.dom.Node;

/**
 * Item transformer for Lone Wolf ruleset.
 * @author Tamas_Szekeres
 */
public class LwRuleBookItemTransformer extends AbstractBookItemTransformer<LwItem> {

    @Override
    protected LwItem getItem(final String id, final String name, final ItemType itemType) {
        return new LwItem(id, fixText(name), itemType);
    }

    @Override
    protected void finishItemCreation(final LwItem item, final Node node) {
        item.setPlacement(Placement.valueOf(extractAttribute(node, "placement")));
        item.setWeaponType(extractAttribute(node, "weaponType"));
        item.setCombatSkill(extractIntegerAttribute(node, "combatSkill", 0));
        final Integer endurance = extractIntegerAttribute(node, "endurance", 0);
        item.setEndurance(endurance);
        item.setInitialEndurance(endurance);

        final EquipInfo equipInfo = item.getEquipInfo();
        if (equipInfo.isEquippable() && item.getItemType() != ItemType.weapon1) {
            equipInfo.setEquipped(true);
        }
    }

}
