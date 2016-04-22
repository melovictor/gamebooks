package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.character.Character;

/**
 * Class for handling the attributes of a hero Character.
 * @author Tamas_Szekeres
 */
public interface AttributeHandler {

    /**
     * Resolves a string expression using the provided characters' properties.
     * @param character the {@link Character} whose properties should be used
     * @param against the expression to be resolved
     * @return the resolved value
     */
    int resolveValue(Character character, String against);

}
