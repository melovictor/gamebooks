package hu.zagor.gamebooks.ff.ff.aod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;

/**
 * Obejct for representing a squadron of units.
 * @author Tamas_Szekeres
 */
public class Ff36AllyCharacter extends FfAllyCharacter {

    /**
     * Empty constructor for the deserialization.
     */
    public Ff36AllyCharacter() {
    }

    /**
     * Basic constructor to set up the squadron.
     * @param name the name of the squadron
     * @param amount the amount of creatures in it
     */
    public Ff36AllyCharacter(final String name, final int amount) {
        super(new FfEnemy());
        final FfEnemy ally = getAlly();
        ally.setName(name);
        setStamina(amount);
    }

}
