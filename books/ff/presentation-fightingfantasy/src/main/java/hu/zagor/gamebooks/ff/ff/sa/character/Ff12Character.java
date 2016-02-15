package hu.zagor.gamebooks.ff.ff.sa.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF12.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff12Character extends FfCharacter {
    private int armour;
    private int initialArmour;

    public int getArmour() {
        return armour;
    }

    public void setArmour(final int armour) {
        this.armour = armour;
    }

    public int getInitialArmour() {
        return initialArmour;
    }

    public void setInitialArmour(final int initialArmour) {
        this.initialArmour = initialArmour;
    }

}
