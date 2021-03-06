package hu.zagor.gamebooks.ff.item;

import hu.zagor.gamebooks.books.contentransforming.item.AbstractBookItemTransformer;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import org.w3c.dom.Node;

/**
 * Item transformer for Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class FfRuleBookItemTransformer extends AbstractBookItemTransformer<FfItem> {

    @Override
    protected FfItem getItem(final String id, final String name, final ItemType itemType) {
        return new FfItem(id, fixText(name), itemType);
    }

    @Override
    protected void finishItemCreation(final FfItem item, final Node node) {
        item.setStaminaDamage(extractIntegerAttribute(node, "staminaDamage", 2));
        item.setSkillDamage(extractIntegerAttribute(node, "skillDamage", 0));

        item.setBaseStaminaDamage(extractIntegerAttribute(node, "baseStaminaDamage", 0));
        item.setDamageProtection(extractIntegerAttribute(node, "damageProtection", 0));

        item.setDose(extractIntegerAttribute(node, "dose", 1));
        item.setPrice(extractIntegerAttribute(node, "price", 0));
        item.setGold(extractIntegerAttribute(node, "gold", 0));

        item.setSkill(extractIntegerAttribute(node, "addToSkill", 0));
        item.setLuck(extractIntegerAttribute(node, "addToLuck", 0));
        item.setStamina(extractIntegerAttribute(node, "addToStamina", 0));
        item.setInitialSkill(extractIntegerAttribute(node, "addToInitialSkill", 0));
        item.setInitialLuck(extractIntegerAttribute(node, "addToInitialLuck", 0));
        item.setInitialStamina(extractIntegerAttribute(node, "addToInitialStamina", 0));
        item.setAttackStrength(extractIntegerAttribute(node, "addToAttackStrength", 0));

        item.setBlessed(extractBooleanAttribute(node, "blessed", false));
        item.setMagical(extractBooleanAttribute(node, "magical", false));

        item.setPreFight(extractBooleanAttribute(node, "preFight", false));
        item.setAtFight(extractBooleanAttribute(node, "atFight", false));

        item.setActions(extractIntegerAttribute(node, "actions", 1));
        final boolean forceEquip = extractBooleanAttribute(node, "forceEquip", false);
        if (forceEquip) {
            item.getEquipInfo().setEquipped(true);
        }
        item.getEquipInfo().setRemovable(extractBooleanAttribute(node, "removable", true));
    }
}
