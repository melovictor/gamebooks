package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Class for wrapping a possible outcome with boundaries for the time it is reached.
 * @author Tamas_Szekeres
 */
public class FightOutcome implements TrueCloneable {

    private int min;
    private int max = Integer.MAX_VALUE;
    private FfParagraphData paragraphData;

    public int getMin() {
        return min;
    }

    public void setMin(final int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(final int max) {
        this.max = max;
    }

    public FfParagraphData getParagraphData() {
        return paragraphData;
    }

    public void setParagraphData(final FfParagraphData paragraphData) {
        this.paragraphData = paragraphData;
    }

    @Override
    public FightOutcome clone() throws CloneNotSupportedException {
        final FightOutcome cloned = (FightOutcome) super.clone();
        cloned.paragraphData = cloneObject(paragraphData);
        return cloned;
    }

    @SuppressWarnings("unchecked")
    private <T extends TrueCloneable> T cloneObject(final T cloneable) throws CloneNotSupportedException {
        T cloned = null;
        if (cloneable != null) {
            cloned = (T) cloneable.clone();
        }
        return cloned;
    }
}
