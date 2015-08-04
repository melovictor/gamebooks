package hu.zagor.gamebooks.content.choice;

import java.io.Serializable;
import java.util.Comparator;

import org.springframework.util.Assert;

/**
 * Comparator that compares two {@link Choice} beans based on their position.
 * @author Tamas_Szekeres
 */
public class ChoicePositionComparator implements Comparator<Choice>, Serializable {

    @Override
    public int compare(final Choice choiceA, final Choice choiceB) {
        Assert.notNull(choiceA, "The parameter 'choiceA' cannot be null!");
        Assert.notNull(choiceB, "The parameter 'choiceB' cannot be null!");

        return (int) Math.signum(choiceA.getPosition() - choiceB.getPosition());
    }
}
