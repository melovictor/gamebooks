package hu.zagor.gamebooks.content.command.userinput.domain;

import hu.zagor.gamebooks.content.ParagraphData;

import java.io.Serializable;

/**
 * Abstract class for storing and evaluating user input responses.
 * @author Tamas_Szekeres
 */
public abstract class UserInputResponse implements Cloneable, Serializable {

    private ParagraphData data;
    private int minResponseTime;
    private int maxResponseTime;

    public ParagraphData getData() {
        return data;
    }

    public void setData(final ParagraphData data) {
        this.data = data;
    }

    /**
     * Checks whether the answer provided by the user matches the requirements of this response.
     * @param answer the user's answer, cannot be null
     * @return true if the answer matches this response, false otherwise
     */
    public abstract boolean matches(String answer);

    /**
     * Returns true if the current response is a fallback response (matching everything in case nothing else matches) or not.
     * @return true if this is a fallback response, false if it is a specific response
     */
    public abstract boolean isFallback();

    @Override
    public UserInputResponse clone() throws CloneNotSupportedException {
        final UserInputResponse cloned = (UserInputResponse) super.clone();
        cloned.setData(data.clone());
        return cloned;
    }

    public int getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(final int minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public int getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(final int maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }
}
