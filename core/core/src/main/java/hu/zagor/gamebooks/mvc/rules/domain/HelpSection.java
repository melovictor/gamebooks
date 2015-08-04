package hu.zagor.gamebooks.mvc.rules.domain;

/**
 * Bean for storing data about a single help section.
 * @author Tamas_Szekeres
 */
public class HelpSection {

    private String jsp;
    private String location;

    public String getJsp() {
        return jsp;
    }

    public void setJsp(final String jsp) {
        this.jsp = jsp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

}
