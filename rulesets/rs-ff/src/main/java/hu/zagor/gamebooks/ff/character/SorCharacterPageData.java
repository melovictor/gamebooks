package hu.zagor.gamebooks.ff.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for storing data to be displayed on the character page.
 * @author Tamas_Szekeres
 */
@Component("sorCharacterPageData")
@Scope("prototype")
public class SorCharacterPageData extends FfCharacterPageData {
    /**
     * Bean for storing data to display on the character page for Fighting Fantasy ruleset.
     * @param character the character
     * @param handler the {@link SorCharacter} to use for obtaining the characters' properties
     */
    public SorCharacterPageData(final SorCharacter character, final FfCharacterHandler handler) {
        super(character, handler);
    }

}
