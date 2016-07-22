package hu.zagor.gamebooks.domain;

/**
 * Bean for storing adventure-specific information about the given book like whether it has inventory or map, etc.
 * @author Tamas_Szekeres
 */
public class LwBookContentSpecification extends BookContentSpecification {
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(final String level) {
        this.level = level;
    }
}
