package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.domain;

/**
 * Bean for returning a response about a single round of Six Pick.
 * @author Tamas_Szekeres
 */
public class SixPickResult {

    private int newGold;
    private String responseText;
    private int status;

    public int getNewGold() {
        return newGold;
    }

    public void setNewGold(final int newGold) {
        this.newGold = newGold;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(final String responseText) {
        this.responseText = responseText;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
