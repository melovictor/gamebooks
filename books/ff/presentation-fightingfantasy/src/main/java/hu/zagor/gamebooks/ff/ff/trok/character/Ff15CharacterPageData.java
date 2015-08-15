package hu.zagor.gamebooks.ff.ff.trok.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.ff.ff.trok.character.domain.Ff15ShipAttributes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF10.
 * @author Tamas_Szekeres
 */
@Component("ff15CharacterPageData")
@Scope("prototype")
public class Ff15CharacterPageData extends FfCharacterPageData {

    private final Ff15ShipAttributes ship;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 15 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff15CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);

        final Ff15Character ff15Character = (Ff15Character) character;

        ship = ff15Character.getShipAttributes();
    }

    public Ff15ShipAttributes getShip() {
        return ship;
    }

}
