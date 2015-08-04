package hu.zagor.gamebooks.content.command.random;

import hu.zagor.gamebooks.content.ParagraphData;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A random throw result bean storing a numeric interval and a paragraph data.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class RandomResult implements Cloneable {

    private int min;
    private int max;
    private ParagraphData paragraphData;

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public ParagraphData getParagraphData() {
        return paragraphData;
    }

    public void setMin(final int min) {
        this.min = min;
    }

    public void setMax(final int max) {
        this.max = max;
    }

    public void setParagraphData(final ParagraphData paragraphData) {
        this.paragraphData = paragraphData;
    }

    @Override
    public RandomResult clone() throws CloneNotSupportedException {
        final RandomResult cloned = (RandomResult) super.clone();

        cloned.paragraphData = paragraphData.clone();

        return cloned;
    }
}
