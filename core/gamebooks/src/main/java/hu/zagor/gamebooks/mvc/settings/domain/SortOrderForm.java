package hu.zagor.gamebooks.mvc.settings.domain;

/**
 * Bean for storing user input about required sorting order.
 * @author Tamas_Szekeres
 */
public class SortOrderForm {
    private String language;
    private String order;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(final String order) {
        this.order = order;
    }
}
