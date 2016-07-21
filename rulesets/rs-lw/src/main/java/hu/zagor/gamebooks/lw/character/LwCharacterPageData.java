package hu.zagor.gamebooks.lw.character;

import hu.zagor.gamebooks.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("lwCharacterPageData")
@Scope("prototype")
public class LwCharacterPageData extends RawCharacterPageData {
    private final LwAttributeHandler attributeHandler;
    private final LwCharacter character;
    private String weaponskillWeapon;

    private final int endurance;
    private final int initialEndurance;
    private final int combatSkill;

    private final List<Item> weapons = new ArrayList<>(2);
    private final List<Item> normalEquipment = new ArrayList<>(8);
    private final List<Item> specialEquipment = new ArrayList<>();
    private final List<Item> shadows = new ArrayList<>();
    private final int gold;

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

    public List<Item> getWeapons() {
        return weapons;
    }

    public List<Item> getNormalEquipment() {
        return normalEquipment;
    }

    public List<Item> getSpecialEquipment() {
        return specialEquipment;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getInitialEndurance() {
        return initialEndurance;
    }

    public List<Item> getShadows() {
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

}
