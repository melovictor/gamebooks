package hu.zagor.gamebooks.content.choice;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import org.springframework.util.Assert;

/**
 * Default implementation of the {@link ChoiceSet} interface.
 * @author Tamas_Szekeres
 */
public class DefaultChoiceSet extends TreeSet<Choice> implements ChoiceSet {

    /**
     * Default package private constructor for serialization-deserialization.
     */
    public DefaultChoiceSet() {
        super(new ChoicePositionComparator());
    }

    /**
     * Creates a new {@link DefaultChoiceSet} object using the supplied comparator to sort the {@link Choice} objects it will contain.
     * @param comparator the {@link Comparator} to use for the sorting
     */
    public DefaultChoiceSet(final Comparator<? super Choice> comparator) {
        super(comparator);
    }

    @Override
    public Choice removeByPosition(final int position) {
        return byPos(position, true);
    }

    @Override
    public Choice getChoiceByPosition(final int position) {
        return byPos(position, false);
    }

    private Choice byPos(final int position, final boolean shouldRemove) {
        Choice found = null;
        final Iterator<Choice> iterator = iterator();
        while (iterator.hasNext() && found == null) {
            final Choice choice = iterator.next();
            if (choice.getPosition() == position) {
                found = choice;
                if (shouldRemove) {
                    iterator.remove();
                }
            }
        }
        return found;
    }

    @Override
    public Choice getChoiceById(final String paragraphId) {
        Assert.notNull(paragraphId, "The parameter 'paragraphId' cannot be null!");
        Choice found = null;
        final Iterator<Choice> iterator = iterator();
        while (iterator.hasNext() && found == null) {
            final Choice choice = iterator.next();
            if (paragraphId.equals(choice.getId())) {
                found = choice;
            }
        }
        return found;
    }

}
