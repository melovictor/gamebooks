package hu.zagor.gamebooks.ff.ff.trok.character.handler.attribute;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.trok.character.Ff15Character;
import hu.zagor.gamebooks.ff.ff.trok.character.domain.Ff15ShipAttributes;

/**
 * Attribute handler for FF15.
 * @author Tamas_Szekeres
 */
public class Ff15AttributeHandler extends FfAttributeHandler {

    @Override
    public void sanityCheck(final FfCharacter characterObject) {
        final Ff15Character character = (Ff15Character) characterObject;

        super.sanityCheck(character);

        final Ff15ShipAttributes shipAttributes = character.getShipAttributes();
        if (shipAttributes.getInitialShield() < shipAttributes.getShield()) {
            shipAttributes.setShield(shipAttributes.getInitialShield());
        }
        if (shipAttributes.getInitialWeaponStrength() < shipAttributes.getWeaponStrength()) {
            shipAttributes.setWeaponStrength(shipAttributes.getInitialWeaponStrength());
        }
    }
}
