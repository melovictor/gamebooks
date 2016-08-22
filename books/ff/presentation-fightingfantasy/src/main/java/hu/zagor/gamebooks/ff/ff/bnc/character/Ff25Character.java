package hu.zagor.gamebooks.ff.ff.bnc.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF25.
 * @author Tamas_Szekeres
 */
@Component("ff25Character")
@Scope("prototype")
public class Ff25Character extends FfCharacter {
    private int willpower;
    private int initialWillpower;

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(final int willpower) {
        this.willpower = willpower;
    }

    public int getInitialWillpower() {
        return initialWillpower;
    }

    public void setInitialWillpower(final int initialWillpower) {
        this.initialWillpower = initialWillpower;
    }

}
