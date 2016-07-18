package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.handler.attribute.LwAttributeHandler;

public class LwCharacterHandler extends CharacterHandler {
    @Override
    public LwAttributeHandler getAttributeHandler() {
        return (LwAttributeHandler) super.getAttributeHandler();
    }
}
