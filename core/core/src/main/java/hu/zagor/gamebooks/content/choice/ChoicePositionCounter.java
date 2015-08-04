package hu.zagor.gamebooks.content.choice;

import hu.zagor.gamebooks.content.Paragraph;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class for storing the choice position counter for a given {@link Paragraph}.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class ChoicePositionCounter {

    private int position;

    /**
     * Gets a new position number based on the request. If the input parameter is null then the next position
     * value will be returned. If it is a number which is smaller than the currently positioned next number
     * then it will simply return that value. If it is higher then it will return the input number and
     * increase the inner counter so the next value requested with a null input parameter will be a unique
     * number (at least during the lifecycle of the {@link ChoicePositionCounter} object).
     * @param inPos the string representation of the value that is being used
     * @return the input number if it wasn't null, or the next biggest position number
     */
    public int updateAndGetPosition(final Integer inPos) {
        int pos;
        if (inPos == null) {
            pos = position++;
        } else {
            pos = inPos;
            if (pos >= position) {
                position = pos + 1;
            }
        }
        return pos;
    }
}
