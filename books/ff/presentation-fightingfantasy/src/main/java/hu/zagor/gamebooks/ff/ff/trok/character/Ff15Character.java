package hu.zagor.gamebooks.ff.ff.trok.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.trok.character.domain.Ff15ShipAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF10.
 * @author Tamas_Szekeres
 */
@Component("ff15Character")
@Scope("prototype")
public class Ff15Character extends FfCharacter {

    @Autowired
    private Ff15ShipAttributes shipAttributes;

    public Ff15ShipAttributes getShipAttributes() {
        return shipAttributes;
    }

    public void setShipAttributes(final Ff15ShipAttributes shipAttributes) {
        this.shipAttributes = shipAttributes;
    }

}
