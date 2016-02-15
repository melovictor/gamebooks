package hu.zagor.gamebooks.ff.ff.sa.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF12.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff12CharacterPageData extends FfCharacterPageData {
    private final int armour;
    private final int initialArmour;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 12 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff12CharacterPageData(final Ff12Character character, final FfCharacterHandler handler) {
        super(character, handler);
        armour = character.getArmour();
        initialArmour = character.getInitialArmour();
    }

    public int getArmour() {
        return armour;
    }

    public int getInitialArmour() {
        return initialArmour;
    }

}
