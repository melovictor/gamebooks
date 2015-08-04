package hu.zagor.gamebooks.ff.ff.votv.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF38.
 * @author Tamas_Szekeres
 */
@Component("ff38Character")
@Scope("prototype")
public class Ff38Character extends FfCharacter {

    private int faith;

    public int getFaith() {
        return faith;
    }

    public void setFaith(final int faith) {
        this.faith = faith;
    }

}
