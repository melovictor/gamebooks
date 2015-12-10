package hu.zagor.gamebooks.ff.character;

import hu.zagor.gamebooks.character.item.Item;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Player character for Sorcery ruleset.
 * @author Tamas_Szekeres
 */
@Component("sorCharacter")
@Scope("prototype")
public class SorCharacter extends FfCharacter {
    private boolean wizard;

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
}
