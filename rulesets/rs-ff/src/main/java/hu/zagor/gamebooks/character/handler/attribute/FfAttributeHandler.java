package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.lang.reflect.Field;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * Class for handling the attributes of a Fighting Fantasy character.
 * @author Tamas_Szekeres
 */
public class FfAttributeHandler {
    @Autowired private ExpressionResolver expressionResolver;
    @LogInject private Logger logger;
    @Autowired private DeductionCalculator deductionCalculator;

    /**
     * Executes the modification requested by the {@link ModifyAttribute} object.
     * @param character the {@link FfCharacter} on which the modification has to be executed
     * @param modAttr the modification to be executed
     */
    public void handleModification(final FfCharacter character, final ModifyAttribute modAttr) {
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(modAttr, "The parameter 'modAttr' cannot be null!");
        final String attribute = modAttr.getAttribute();
        final int amount = modAttr.getAmount();
        final ModifyAttributeType type = modAttr.getType();
        if (modAttr.isGoldOnly()) {
            handleRegularFieldChange(character, attribute, amount, type);
        } else {
            handleModification(character, attribute, amount, type);
        }
    }

    /**
     * Executes the modification requested.
     * @param character the {@link FfCharacter} on which the modification has to be executed
     * @param attribute the attribute that has to be modified
     * @param amount the amount by which the attribute has to be modified
     */
    public void handleModification(final FfCharacter character, final String attribute, final int amount) {
        handleModification(character, attribute, amount, ModifyAttributeType.change);
    }

    /**
     * Executes the modification requested.
     * @param character the {@link FfCharacter} on which the modification has to be executed
     * @param attribute the attribute that has to be modified
     * @param amount the amount by which the attribute has to be modified
     * @param type the modification type
     */
    public void handleModification(final FfCharacter character, final String attribute, final int amount, final ModifyAttributeType type) {
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(attribute, "The parameter 'attribute' cannot be null!");
        if ("gold".equals(attribute) && type == ModifyAttributeType.change && amount < 0) {
            handleGoldFieldChange(character, amount);
        } else {
            handleRegularFieldChange(character, attribute, amount, type);
        }
    }

    private void handleGoldFieldChange(final FfCharacter character, final int amount) {
        final GoldItemDeduction deduction = deductionCalculator.calculateDeductibleElements(character, -amount);
        character.setGold(character.getGold() - deduction.getGold());
        final List<Item> equipment = character.getEquipment();
        for (final FfItem item : deduction.getItems()) {
            equipment.remove(item);
        }
    }

    private void handleRegularFieldChange(final FfCharacter character, final String attribute, final int amount, final ModifyAttributeType type) {
        try {
            final Field field = ReflectionUtils.findField(character.getClass(), attribute);
            if (field == null) {
                throw new NoSuchFieldException();
            }
            field.setAccessible(true);
            if (type == ModifyAttributeType.change) {
                ReflectionUtils.setField(field, character, field.getInt(character) + amount);
            } else {
                ReflectionUtils.setField(field, character, amount);
            }
            field.setAccessible(false);
            sanityCheck(character);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            logger.error("Failed to alter field '{}' on object type '{}'.", attribute, character.getClass().toString());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Makes sure that the attributes of the character are withing the allowed boundaries.
     * @param character the {@link FfCharacter} to check
     */
    public void sanityCheck(final FfCharacter character) {
        if (character.getStamina() > character.getInitialStamina()) {
            character.setStamina(character.getInitialStamina());
        }
        if (character.getSkill() > character.getInitialSkill()) {
            character.setSkill(character.getInitialSkill());
        }
        if (character.getLuck() > character.getInitialLuck()) {
            character.setLuck(character.getInitialLuck());
        }
        if (character.getLuck() < 0) {
            character.setLuck(0);
        }
        if (character.getGold() < 0) {
            character.setGold(0);
        }
    }

    /**
     * Resolves a string expression using the provided characters' properties.
     * @param character the {@link FfCharacter} whose properties should be used
     * @param against the expression to be resolved
     * @return the resolved value
     */
    public int resolveValue(final FfCharacter character, final String against) {
        return expressionResolver.resolveValue(character, against);
    }

    /**
     * Checks whether the provided character is alive or not.
     * @param character the {@link FfCharacter}
     * @return true if the character is alive, false otherwise
     */
    public boolean isAlive(final FfCharacter character) {
        boolean alive;
        if (character instanceof FfAllyCharacter) {
            alive = isAlive((FfAllyCharacter) character);
        } else {
            alive = resolveValue(character, "stamina") > 0;
        }
        return alive;
    }

    /**
     * Checks whether the provided character is alive or not.
     * @param character the {@link FfCharacter}
     * @return true if the character is alive, false otherwise
     */
    public boolean isAlive(final FfAllyCharacter character) {
        return character.getStamina() > 0;
    }

}
