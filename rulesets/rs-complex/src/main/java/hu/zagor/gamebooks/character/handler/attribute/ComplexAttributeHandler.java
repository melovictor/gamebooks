package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.slf4j.Logger;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * Attribute handler for the complex rulesets which can alter regular fields on an object.
 * @author Tamas_Szekeres
 * @param <C> the actual {@link Character} type
 */
public abstract class ComplexAttributeHandler<C extends Character> extends DefaultAttributeHandler {
    @LogInject private Logger logger;

    /**
     * Handles modifications in the attributes of the {@link Character} object.
     * @param character the {@link Character} object to modify
     * @param attribute the attribute to change
     * @param amount the amount by which to change the attribute
     */
    public void handleModification(final C character, final String attribute, final int amount) {
        handleModification(character, attribute, amount, ModifyAttributeType.change);
    }

    /**
     * Handles modifications in the attributes of the {@link Character} object.
     * @param character the {@link Character} object to modify
     * @param attribute the attribute to change
     * @param amount the amount by which to change the attribute
     * @param type the type of the change
     */
    public abstract void handleModification(final C character, final String attribute, final int amount, final ModifyAttributeType type);

    void handleRegularFieldChange(final Object character, final String attribute, final int amount, final ModifyAttributeType type) {
        handleRegularFieldChange(character, attribute.split("\\."), amount, type);
    }

    private void handleRegularFieldChange(final Object object, final String[] attribute, final int amount, final ModifyAttributeType type) {
        if (attribute.length == 1) {
            changeRegularField(object, attribute[0], amount, type);
        } else {
            final Object nextLevelObject = fetchObjectFromField(object, attribute[0]);
            handleRegularFieldChange(nextLevelObject, Arrays.copyOfRange(attribute, 1, attribute.length), amount, type);
        }

    }

    private Object fetchObjectFromField(final Object object, final String attribute) {
        try {
            final Field field = ReflectionUtils.findField(object.getClass(), attribute);
            if (field == null) {
                throw new NoSuchFieldException();
            }
            field.setAccessible(true);
            final Object next = ReflectionUtils.getField(field, object);
            field.setAccessible(false);

            return next;
        } catch (NoSuchFieldException | IllegalArgumentException ex) {
            logger.error("Failed to fetch contents from field '{}' on object type '{}'.", attribute, object.getClass().toString());
            throw new RuntimeException(ex);
        }
    }

    private void changeRegularField(final Object object, final String attribute, final int amount, final ModifyAttributeType type) {
        try {
            final Field field = ReflectionUtils.findField(object.getClass(), attribute);
            if (field == null) {
                final Method setMethod = ReflectionUtils.findMethod(object.getClass(), "set" + StringUtils.capitalize(attribute), (Class<?>[]) null);
                final Method getMethod = ReflectionUtils.findMethod(object.getClass(), "get" + StringUtils.capitalize(attribute), (Class<?>[]) null);
                if (setMethod == null || getMethod == null) {
                    throw new NoSuchFieldException("Couldn't find field '" + attribute + "' on object '" + object + "', nor a getter and setter for it.");
                }

                getMethod.setAccessible(true);
                setMethod.setAccessible(true);
                if (type == ModifyAttributeType.change) {
                    ReflectionUtils.invokeMethod(setMethod, object, ((int) ReflectionUtils.invokeMethod(getMethod, object)) + amount);
                } else {
                    ReflectionUtils.invokeMethod(setMethod, object, amount);
                }
                getMethod.setAccessible(false);
                setMethod.setAccessible(false);
            } else {
                setThroughField(object, amount, type, field);
            }
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            logger.error("Failed to alter field '{}' on object type '{}'.", attribute, object.getClass().toString());
            throw new RuntimeException(ex);
        }
    }

    private void setThroughField(final Object object, final int amount, final ModifyAttributeType type, final Field field) throws IllegalAccessException {
        field.setAccessible(true);
        if (type == ModifyAttributeType.change) {
            ReflectionUtils.setField(field, object, field.getInt(object) + amount);
        } else {
            ReflectionUtils.setField(field, object, amount);
        }
        field.setAccessible(false);
    }
}
