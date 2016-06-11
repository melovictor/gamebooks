package hu.zagor.gamebooks.ff.ff.sots.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF20.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff20Character extends FfCharacter {
    private static final int NAKED_EXTRA_DAMAGE = -2;
    private static final int INITIAL_HONOR = 3;

    private int honor = INITIAL_HONOR;
    private SpecialSkill specialSkill;

    /**
     * Default constructor setting an initial damage protection that can only be countered by an armour.
     */
    public Ff20Character() {
        setDamageProtection(NAKED_EXTRA_DAMAGE);
    }

    public int getHonor() {
        return honor;
    }

    public void setHonor(final int honor) {
        this.honor = honor;
    }

    public SpecialSkill getSpecialSkill() {
        return specialSkill;
    }

    public void setSpecialSkill(final SpecialSkill specialSkill) {
        this.specialSkill = specialSkill;
    }

}
