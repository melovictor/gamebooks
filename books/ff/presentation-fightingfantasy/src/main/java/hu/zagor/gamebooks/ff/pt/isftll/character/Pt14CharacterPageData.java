package hu.zagor.gamebooks.ff.pt.isftll.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for PT14.
 * @author Tamas_Szekeres
 */
@Component("pt14CharacterPageData")
@Scope("prototype")
public class Pt14CharacterPageData extends FfCharacterPageData {

    private final int wisdom;

    /**
     * Bean for storing data to display on the character page for Proteus magazine 14.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Pt14CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);

        final FfAttributeHandler attributeHandler = handler.getAttributeHandler();
        wisdom = attributeHandler.resolveValue(character, "wisdom");
    }

    public int getWisdom() {
        return wisdom;
    }
}
