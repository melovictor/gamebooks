package hu.zagor.gamebooks.ff.ff.trok.mvc.books.newgame.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.trok.character.Ff15Character;
import hu.zagor.gamebooks.ff.ff.trok.character.domain.Ff15ShipAttributes;
import hu.zagor.gamebooks.ff.mvc.book.newgame.service.DefaultFfCharacterGenerator;
import java.util.Map;

/**
 * Character generator for FF15.
 * @author Tamas_Szekeres
 */
public class Ff15CharacterGenerator extends DefaultFfCharacterGenerator {

    private static final int DICE_SIDE = 6;
    private static final int WEAPON_DEFAULT = 6;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookContentSpecification bookContentSpecification, final BookInformations info) {
        final Map<String, Object> generateCharacter = super.generateCharacter(characterObject, bookContentSpecification, info);

        final Ff15Character character = (Ff15Character) characterObject;

        final int[] shield = getRand().getRandomNumber(1);
        final int[] weaponStrength = getRand().getRandomNumber(1, WEAPON_DEFAULT);

        final Ff15ShipAttributes shipAttributes = character.getShipAttributes();
        shipAttributes.setShield(shield[0]);
        shipAttributes.setInitialShield(shield[0]);

        shipAttributes.setWeaponStrength(weaponStrength[0]);
        shipAttributes.setInitialWeaponStrength(weaponStrength[0]);

        shipAttributes.setSmartMissile(2);

        generateCharacter.put("ffShield", shipAttributes.getInitialShield() + getDiceRenderer().render(DICE_SIDE, shield));
        generateCharacter.put("ffWeaponStrength", shipAttributes.getInitialWeaponStrength() + getDiceRenderer().render(DICE_SIDE, weaponStrength));

        return generateCharacter;
    }

}
