package hu.zagor.gamebooks.ff.ff.hoh.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF10.
 * @author Tamas_Szekeres
 */
@Component("ff10Character")
@Scope("prototype")
public class Ff10Character extends FfCharacter {

    private int fear;
    private int initialFear;

    public int getFear() {
        return fear;
    }

    public void setFear(final int fear) {
        this.fear = fear;
    }

    public int getInitialFear() {
        return initialFear;
    }

    public void setInitialFear(final int initialFear) {
        this.initialFear = initialFear;
    }

}
