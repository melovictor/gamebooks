package hu.zagor.gamebooks.ff.ff.hoh.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF10.
 * @author Tamas_Szekeres
 */
@Component("ff10CharacterPageData")
@Scope("prototype")
public class Ff10CharacterPageData extends FfCharacterPageData {

    private final int fear;
    private final int initialFear;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 10 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff10CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);

        final FfAttributeHandler attributeHandler = handler.getAttributeHandler();
        initialFear = attributeHandler.resolveValue(character, "initialFear");
        fear = attributeHandler.resolveValue(character, "fear");
    }

    public int getFear() {
        return fear;
    }

    public int getInitialFear() {
        return initialFear;
    }
}
