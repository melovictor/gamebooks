package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.lw.character.LwCharacter;

public class LwAttributeHandler extends DefaultAttributeHandler {

    public boolean isAlive(final LwCharacter character) {
        return resolveValue(character, "endurance") > 0;
    }

}
