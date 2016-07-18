package hu.zagor.gamebooks.lw.character;

import hu.zagor.gamebooks.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("lwCharacterPageData")
@Scope("prototype")
public class LwCharacterPageData extends RawCharacterPageData {
    private final LwAttributeHandler attributeHandler;
    private final LwCharacter character;

    /**
     * Bean for storing data to display on the character page for Lone Wolf ruleset.
     * @param character the character
     * @param handler the {@link LwCharacterHandler} to use for obtaining the characters' properties
     */
    public LwCharacterPageData(final LwCharacter character, final LwCharacterHandler handler) {
        super(character);

        this.attributeHandler = handler.getAttributeHandler();
        this.character = character;
    }

    public boolean isAlive() {
        return !character.isInitialized() || attributeHandler.isAlive(character);
    }

}
