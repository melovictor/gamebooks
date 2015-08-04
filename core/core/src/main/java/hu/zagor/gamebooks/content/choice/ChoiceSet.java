package hu.zagor.gamebooks.content.choice;

import java.util.Set;
import java.util.SortedSet;

/**
 * Interface for a {@link SortedSet} containing {@link Choice} elements from which elements can be extracted by position values.
 * @author Tamas_Szekeres
 */
public interface ChoiceSet extends SortedSet<Choice> {

    /**
     * Searches for the first {@link Choice} with the given position value and returns it. If a choice with the provided position cannot be found it returns null.
     * @param position the position we're looking for
     * @return the {@link Choice} object with the appropriate position or null if it doesn't exists in this {@link Set}
     */
    Choice getChoiceByPosition(int position);

    /**
     * Searches for the first {@link Choice} with the given id value and returns it. If a choice with the provided id cannot be found it returns null.
     * @param paragraphId the id we're looking for, cannot be null
     * @return the {@link Choice} object with the appropriate id or null if it doesn't exists in this {@link Set}
     */
    Choice getChoiceById(String paragraphId);

    /**
     * Searches for the first {@link Choice} with the given position value and removes it from the collection. If a choice with the provided position cannot be found it returns
     * null.
     * @param position the position we're looking for
     * @return the {@link Choice} object with the appropriate position or null if it doesn't exists in this {@link Set}
     */
    Choice removeByPosition(int position);

}
