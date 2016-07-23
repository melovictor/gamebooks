package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.WeaponReplacementData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.StringUtils;
import org.w3c.dom.Node;

/**
 * Class for transforming the "fight" elements of a paragraph.
 * @author Tamas_Szekeres
 */
public class FfFightTransformer extends ComplexFightTransformer<FfEnemy, FfFightCommand> {

    @Override
    void parseRulesetSpecificAttributes(final FfFightCommand ffFightCommand, final Node node) {
        ffFightCommand.setBattleType(extractAttribute(node, "type"));
        ffFightCommand.setResolver(extractAttribute(node, "resolver", "basic"));
        ffFightCommand.setForceOrder(extractBooleanAttribute(node, "forceOrder", false));
        ffFightCommand.setAttackStrengthRolledDices(extractIntegerAttribute(node, "attackStrengthRolledDices", 2));
        ffFightCommand.setAttackStrengthUsedDices(extractIntegerAttribute(node, "attackStrengthUsedDices", 2));
        ffFightCommand.setRecoverDamage(extractBooleanAttribute(node, "recoverDamage", false));
        ffFightCommand.setAllyStaminaVisible(extractBooleanAttribute(node, "allyStaminaVisible", false));

        ffFightCommand.setUsableWeaponTypes(
            toWeaponTypes(extractAttribute(node, "usableWeaponTypes", "shooting".equals(ffFightCommand.getBattleType()) ? "shooting" : "weapon1,weapon2").split(",")));
        final String forceWeapon = extractAttribute(node, "forceWeapon");
        if (!StringUtils.isEmpty(forceWeapon)) {
            final WeaponReplacementData replacementData = new WeaponReplacementData();
            final ArrayList<String> forcedWeapons = new ArrayList<>();
            forcedWeapons.addAll(Arrays.asList(forceWeapon.split(",")));
            replacementData.setForceWeapons(forcedWeapons);
            ffFightCommand.setReplacementData(replacementData);
        }
        ffFightCommand.setPreFightAvailable(extractBooleanAttribute(node, "preFight", true));
    }

    private List<ItemType> toWeaponTypes(final String[] weaponTypeNames) {
        final List<ItemType> usableWepaonTypes = new ArrayList<>();

        for (final String typeName : weaponTypeNames) {
            usableWepaonTypes.add(ItemType.valueOf(typeName));
        }

        return usableWepaonTypes;
    }

    @Override
    FfFightCommand getFightCommandBean() {
        return getBeanFactory().getBean(FfFightCommand.class);
    }

}
