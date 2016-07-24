package hu.zagor.gamebooks.lw.mvc.book.section.domain;

/**
 * Form containing the parameters passed in by the user for the fighting.
 * @author Tamas_Szekeres
 */
public class LwFightCommandForm {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
