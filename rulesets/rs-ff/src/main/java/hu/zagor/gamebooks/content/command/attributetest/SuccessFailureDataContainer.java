package hu.zagor.gamebooks.content.command.attributetest;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Object for storing success/failure result details.
 * @author Tamas_Szekeres
 */
public class SuccessFailureDataContainer implements TrueCloneable {
    private FfParagraphData data;
    private Integer rolled;

    /**
     * Default constructor for the Spring deserializer.
     */
    SuccessFailureDataContainer() {
    }

    /**
     * Basic constructor to set all fields with.
     * @param data the {@link FfParagraphData} object
     * @param rolled the value rolled at which this data must kick in
     */
    public SuccessFailureDataContainer(final FfParagraphData data, final Integer rolled) {
        this.data = data;
        this.rolled = rolled;
    }

    public FfParagraphData getData() {
        return data;
    }

    public void setData(final FfParagraphData data) {
        this.data = data;
    }

    public Integer getRolled() {
        return rolled;
    }

    public void setRolled(final Integer rolled) {
        this.rolled = rolled;
    }

    @Override
    public SuccessFailureDataContainer clone() throws CloneNotSupportedException {
        final SuccessFailureDataContainer cloned = (SuccessFailureDataContainer) super.clone();
        cloned.data = data.clone();
        return cloned;
    }
}
