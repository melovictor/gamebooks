package hu.zagor.gamebooks.lw.character;

import hu.zagor.gamebooks.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.character.item.LwItem;
import hu.zagor.gamebooks.character.item.Placement;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for storing data to display on the character page on the page.
 * @author Tamas_Szekeres
 */
@Component("lwCharacterPageData")
@Scope("prototype")
public class LwCharacterPageData extends RawCharacterPageData {
    private final LwAttributeHandler attributeHandler;
    private final LwCharacter character;
    private String weaponskillWeapon;

    private final int endurance;
    private final int initialEndurance;
    private final int combatSkill;

    private final List<LwItem> weapons = new ArrayList<>(2);
    private final List<LwItem> normalEquipment = new ArrayList<>(8);
    private final List<LwItem> specialEquipment = new ArrayList<>();
    private final List<LwItem> shadows = new ArrayList<>();
    private final List<LwItem> preFightItems = new ArrayList<>();
    private final int gold;
    private int oldManExchange;

    /**
     * Bean for storing data to display on the character page for Lone Wolf ruleset.
     * @param character the character
     * @param handler the {@link LwCharacterHandler} to use for obtaining the characters' properties
     */
    public LwCharacterPageData(final LwCharacter character, final LwCharacterHandler handler) {
        super(character);

        this.attributeHandler = handler.getAttributeHandler();
        this.character = character;

        endurance = attributeHandler.resolveValue(character, "endurance");
        initialEndurance = attributeHandler.resolveValue(character, "initialEndurance");
        combatSkill = attributeHandler.resolveValue(character, "combatSkill");
        gold = character.getMoney().getGoldCrowns();

        final Weaponskill weaponskill = character.getKaiDisciplines().getWeaponskill();
        if (weaponskill.isWeaponskillObtained()) {
            weaponskillWeapon = obtainLearnedWeaponName(weaponskill);
        }

        fillItems();
    }

    private void fillItems() {
        LwItem defWpn = null;

        for (final Item itemObject : character.getEquipment()) {
            final LwItem item = (LwItem) itemObject;
            if ("defWpn".equals(item.getId())) {
                defWpn = item;
            } else {
                if (item.isPreFight()) {
                    preFightItems.add(item);
                }
                if (item.getItemType() == ItemType.shadow) {
                    shadows.add(item);
                } else if (item.getPlacement() == Placement.weapon) {
                    weapons.add(item);
                } else if (item.getPlacement() == Placement.special) {
                    specialEquipment.add(item);
                } else if (item.getPlacement() == Placement.backpack) {
                    normalEquipment.add(item);
                }
            }
        }

        if (weapons.isEmpty() && defWpn != null) {
            weapons.add(defWpn);
        }
    }

    private String obtainLearnedWeaponName(final Weaponskill weaponskill) {
        String name = "";
        for (final Field field : Weaponskill.class.getDeclaredFields()) {
            if (Boolean.TYPE.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    if ((boolean) field.get(weaponskill)) {
                        name = field.getName();
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException("Couldn't fetch value from weaponskill field.");
                }
                field.setAccessible(false);
            }
        }
        return name;
    }

    public boolean isAlive() {
        return !character.isInitialized() || attributeHandler.isAlive(character);
    }

    public List<LwItem> getWeapons() {
        return weapons;
    }

    public List<LwItem> getNormalEquipment() {
        return normalEquipment;
    }

    public List<LwItem> getSpecialEquipment() {
        return specialEquipment;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getInitialEndurance() {
        return initialEndurance;
    }

    public List<LwItem> getShadows() {
        return shadows;
    }

    public int getGold() {
        return gold;
    }

    public int getCombatSkill() {
        return combatSkill;
    }

    public LwCharacter getCharacter() {
        return character;
    }

    public String getWeaponskillWeapon() {
        return weaponskillWeapon;
    }

    public int getOldManExchange() {
        return oldManExchange;
    }

    public void setOldManExchange(final int oldManExchange) {
        this.oldManExchange = oldManExchange;
    }

    public List<LwItem> getPreFightItems() {
        return preFightItems;
    }

}
