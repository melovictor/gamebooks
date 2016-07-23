package hu.zagor.gamebooks.ff.ff.tcoc.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF2.
 * @author Tamas_Szekeres
 */
@Component("ff2Character")
@Scope("prototype")
public class Ff2Character extends FfCharacter {

    private int initialSpell;
    private String lastSpellCast;

    public int getInitialSpell() {
        return initialSpell;
    }

    public void setInitialSpell(final int initialSpell) {
        this.initialSpell = initialSpell;
    }

    public String getLastSpellCast() {
        return lastSpellCast;
    }

    public void setLastSpellCast(final String lastSpellCast) {
        this.lastSpellCast = lastSpellCast;
    }

}
