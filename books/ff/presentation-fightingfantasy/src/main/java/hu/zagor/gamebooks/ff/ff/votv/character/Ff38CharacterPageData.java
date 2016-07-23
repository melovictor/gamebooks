package hu.zagor.gamebooks.ff.ff.votv.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF38.
 * @author Tamas_Szekeres
 */
@Component("ff38CharacterPageData")
@Scope("prototype")
public class Ff38CharacterPageData extends FfCharacterPageData {

    private final int faith;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 38 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff38CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);

        final FfAttributeHandler attributeHandler = handler.getAttributeHandler();
        faith = attributeHandler.resolveValue(character, "faith");
    }

    public int getFaith() {
        return faith;
    }

}
