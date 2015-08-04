package hu.zagor.gamebooks.raw.mvc.book.section.domain;

/**
 * Response form for when the user is giving a response on the form.
 * @author Tamas_Szekeres
 */
public class UserInputResponseForm {

    private String responseText;
    private int elapsedTime;
    private int forcedTime;

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(final String responseText) {
        this.responseText = responseText;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(final int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getForcedTime() {
        return forcedTime;
    }

    public void setForcedTime(final int forcedTime) {
        this.forcedTime = forcedTime;
    }

}
