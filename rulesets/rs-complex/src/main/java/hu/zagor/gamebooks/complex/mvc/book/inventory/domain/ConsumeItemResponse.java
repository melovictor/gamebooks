package hu.zagor.gamebooks.complex.mvc.book.inventory.domain;

/**
 * Bean storing response data for item consumption.
 * @author Tamas_Szekeres
 */
public class ConsumeItemResponse {
    private String message;
    private String onclick;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(final String onclick) {
        this.onclick = onclick;
    }

}
