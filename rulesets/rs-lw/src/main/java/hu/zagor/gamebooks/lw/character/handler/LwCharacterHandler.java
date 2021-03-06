package hu.zagor.gamebooks.lw.character.handler;

import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.lw.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.lw.character.handler.item.LwCharacterItemHandler;
import hu.zagor.gamebooks.lw.character.handler.userinteraction.LwUserInteractionHandler;

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

    @Override
    public LwUserInteractionHandler getInteractionHandler() {
        return (LwUserInteractionHandler) super.getInteractionHandler();
    }

}
