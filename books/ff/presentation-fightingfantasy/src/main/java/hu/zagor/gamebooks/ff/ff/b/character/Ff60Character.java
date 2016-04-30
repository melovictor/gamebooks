package hu.zagor.gamebooks.ff.ff.b.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character for the FF60 book.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff60Character extends FfCharacter {
    private int time;
    private int arrowScore;
    private int arrowRound;

    public int getTime() {
        return time;
    }

    public void setTime(final int time) {
        this.time = time;
    }

    public int getArrowScore() {
        return arrowScore;
    }

    public void setArrowScore(final int arrowScore) {
        this.arrowScore = arrowScore;
    }

    public int getArrowRound() {
        return arrowRound;
    }

    public void setArrowRound(final int arrowRound) {
        this.arrowRound = arrowRound;
    }

}
