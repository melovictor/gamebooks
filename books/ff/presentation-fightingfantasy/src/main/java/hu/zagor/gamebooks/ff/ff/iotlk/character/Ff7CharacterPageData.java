package hu.zagor.gamebooks.ff.ff.iotlk.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF7.
 * @author Tamas_Szekeres
 */
@Component("ff7CharacterPageData")
@Scope("prototype")
public class Ff7CharacterPageData extends FfCharacterPageData {

    private final boolean encounteredMap;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 15 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff7CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);
        encounteredMap = character.getParagraphs().contains("149");
    }

    public boolean isEncounteredMap() {
        return encounteredMap;
    }

}
