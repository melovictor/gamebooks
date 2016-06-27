package hu.zagor.gamebooks.ff.character;

import hu.zagor.gamebooks.character.item.Item;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Player character for Sorcery ruleset.
 * @author Tamas_Szekeres
 */
@Component("sorCharacter")
@Scope("prototype")
public class SorCharacter extends FfCharacter {
    private final Map<String, SorCharacter> characterSaveLocations = new HashMap<>();
    private boolean wizard;
    private int lastEatenBonus;
    private boolean luckCookieActive;
    /**
     * An alternative "currency" for making purchases with Fenestra.
     */
    private int magicItem;

    /**
     * Initializes the save locations for wizards for future use.
     */
    public void initializeSaveLocations() {
        characterSaveLocations.put("1-1", null); // 407
        characterSaveLocations.put("1-195", null); // 229

        characterSaveLocations.put("2-1", null);
        characterSaveLocations.put("2-110", null); // 518
        characterSaveLocations.put("2-254", null); // 500

        characterSaveLocations.put("3-1", null);
        characterSaveLocations.put("3-48", null); // 733
        characterSaveLocations.put("3-136", null); // 579

        characterSaveLocations.put("4-1", null);
        characterSaveLocations.put("4-539", null); // 30
        characterSaveLocations.put("4-102", null); // 75
        characterSaveLocations.put("4-321", null); // 631
    }

    public boolean isWizard() {
        return wizard;
    }

    public void setWizard(final boolean wizard) {
        this.wizard = wizard;
    }

    @Override
    public int getSkill() {
        int skill = super.getSkill();
        if (shouldDouble()) {
            skill *= 2;
        }
        return skill;
    }

    @Override
    public int getInitialSkill() {
        int initialSkill = super.getInitialSkill();
        if (shouldDouble()) {
            initialSkill *= 2;
        }
        return initialSkill;
    }

    private boolean shouldDouble() {
        return hasItem("4003");
    }

    private boolean hasItem(final String itemId) {
        boolean hasItem = false;
        for (final Item item : getEquipment()) {
            hasItem |= itemId.equals(item.getId());
        }
        return hasItem;
    }

    public boolean isUsedLibra() {
        return !hasItem("4103");
    }

    public Map<String, SorCharacter> getCharacterSaveLocations() {
        return characterSaveLocations;
    }

    public int getLastEatenBonus() {
        return lastEatenBonus;
    }

    public void setLastEatenBonus(final int lastEatenBonus) {
        this.lastEatenBonus = lastEatenBonus;
    }

    public boolean isLuckCookieActive() {
        return luckCookieActive;
    }

    public void setLuckCookieActive(final boolean luckCookieActive) {
        this.luckCookieActive = luckCookieActive;
    }

    public int getMagicItem() {
        return magicItem;
    }

    public void setMagicItem(final int magicItem) {
        this.magicItem = magicItem;
    }

    @Override
    public SorCharacter clone() throws CloneNotSupportedException {
        return (SorCharacter) super.clone();
    }
}
