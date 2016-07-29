package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import org.springframework.util.Assert;

/**
 * Class for handling the attributes of a Lone Wolf character.
 * @author Tamas_Szekeres
 */
public class LwAttributeHandler extends ComplexAttributeHandler<LwCharacter> {

    /**
     * Checks whether the character is still alive.
     * @param character the {@link LwCharacter} to check
     * @return true if the character is still alive, false if it is dead
     */
    public boolean isAlive(final LwCharacter character) {
        return resolveValue(character, "endurance") > 0;
    }

    /**
     * Handles modifications in the attributes of the {@link LwCharacter} object.
     * @param character the {@link LwCharacter} object to modify
     * @param modAttr the modification to make
     */
    public void handleModification(final LwCharacter character, final ModifyAttribute modAttr) {
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(modAttr, "The parameter 'modAttr' cannot be null!");
        final String attribute = modAttr.getAttribute();
        final int amount = modAttr.getAmount();
        final ModifyAttributeType type = modAttr.getType();
        handleModification(character, attribute, amount, type);
    }

    /**
     * Handles modifications in the attributes of the {@link LwCharacter} object.
     * @param character the {@link LwCharacter} object to modify
     * @param attribute the attribute to change
     * @param amount the amount by which to change the attribute
     * @param type the type of the change
     */
    @Override
    public void handleModification(final LwCharacter character, final String attribute, final int amount, final ModifyAttributeType type) {
        handleRegularFieldChange(character, attribute, amount, type);
        sanityCheck(character);
    }

    /**
     * Does a sanity check on the character.
     * @param character the {@link LwCharacter} to check
     */
    public void sanityCheck(final LwCharacter character) {
        if (character.getEndurance() > character.getInitialEndurance()) {
            character.setEndurance(character.getInitialEndurance());
        }
        final int lostEndurance = character.getInitialEndurance() - character.getEndurance();
        if (lostEndurance < character.getEnduranceLostInCombat()) {
            character.setEnduranceLostInCombat(lostEndurance);
        }
    }

}
