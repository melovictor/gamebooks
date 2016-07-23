package hu.zagor.gamebooks.ff.pt.isftll.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for PT14.
 * @author Tamas_Szekeres
 */
@Component("pt14Character")
@Scope("prototype")
public class Pt14Character extends FfCharacter {

    private int wisdom;

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(final int wisdom) {
        this.wisdom = wisdom;
    }

}
