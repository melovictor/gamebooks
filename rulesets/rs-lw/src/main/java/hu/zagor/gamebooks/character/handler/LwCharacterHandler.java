package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.LwCharacterItemHandler;

/**
 * Lone Wolf-specific {@link CharacterHandler} object.
 * @author Tamas_Szekeres
 */
public class LwCharacterHandler extends CharacterHandler {
    @Override
    public LwAttributeHandler getAttributeHandler() {
        return (LwAttributeHandler) super.getAttributeHandler();
    }

    @Override
    public LwCharacterItemHandler getItemHandler() {
        return (LwCharacterItemHandler) super.getItemHandler();
    }
}
