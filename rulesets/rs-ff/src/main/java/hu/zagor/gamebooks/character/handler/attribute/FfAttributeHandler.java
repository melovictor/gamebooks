package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * Class for handling the attributes of a Fighting Fantasy character.
 * @author Tamas_Szekeres
 */
public class FfAttributeHandler {

    private static final Pattern RESOLVE_PROPERTIES = Pattern.compile("([a-zA-Z]+)");

    @LogInject
    private Logger logger;

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
        handleModification(character, attribute, amount);
    }

    /**
     * Executes the modification requested.
     * @param character the {@link FfCharacter} on which the modification has to be executed
     * @param attribute the attribute that has to be modified
     * @param amount the amount by which the attribute has to be modified
     */
    public void handleModification(final FfCharacter character, final String attribute, final int amount) {
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(attribute, "The parameter 'attribute' cannot be null!");
        try {
            final Field field = ReflectionUtils.findField(character.getClass(), attribute);
            if (field == null) {
                throw new NoSuchFieldException();
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, character, field.getInt(character) + amount);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            logger.error("Failed to alter field '{}' on object type '{}'.", attribute, character.getClass().toString());
            throw new RuntimeException(ex);
        }
        sanityCheck(character);
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
        String expression = against.trim();

        expression = resolveAttributes(expression, character);
        return ((Number) MVEL.eval(expression)).intValue();
    }

    private String resolveAttributes(final String expression, final FfCharacter character) {
        final Matcher matcher = RESOLVE_PROPERTIES.matcher(expression);
        String resolvedExpresssion = expression;

        while (matcher.find()) {
            final String property = matcher.group(1);
            try {
                Integer value = getFieldValue(character, property, true);
                for (final Item item : character.getEquipment()) {
                    value += getItemFieldValue(item, property);
                }

                if (!expression.startsWith("initial")) {
                    int initialValue;
                    try {
                        initialValue = Integer.valueOf(resolveAttributes("initial" + property.substring(0, 1).toUpperCase() + property.substring(1), character));
                    } catch (final IllegalArgumentException ex) {
                        initialValue = Integer.MAX_VALUE;
                    }
                    if (initialValue < value) {
                        value = initialValue;
                    }
                }

                resolvedExpresssion = resolvedExpresssion.replace(property, value.toString());
            } catch (final ReflectiveOperationException e) {
                logger.error("Cannot resolve property '{}'.", property);
                throw new IllegalArgumentException(e);
            }
        }

        return resolvedExpresssion;
    }

    private int getItemFieldValue(final Item item, final String property) throws IllegalArgumentException, ReflectiveOperationException {
        int value = 0;
        if (item.getEquipInfo().isEquipped()) {
            value = getFieldValue(item, property, false);
        }
        return value;
    }

    private int getFieldValue(final Object object, final String property, final boolean triggerException) throws IllegalArgumentException, ReflectiveOperationException {
        final Field field = ReflectionUtils.findField(object.getClass(), property);
        int value = 0;
        if (field != null) {
            field.setAccessible(true);
            value = field.getInt(object);
            field.setAccessible(false);
        } else if (triggerException) {
            throw new NoSuchFieldException();
        }
        return value;
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
