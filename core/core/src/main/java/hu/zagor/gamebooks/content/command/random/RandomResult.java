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

    private String min;
    private String max;
    private ParagraphData paragraphData;

    public ParagraphData getParagraphData() {
        return paragraphData;
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

    public String getMin() {
        return min;
    }

    public void setMin(final String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(final String max) {
        this.max = max;
    }

}
