package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Bean for storing information about special attacks available at a specific situation.
 * @author Tamas_Szekeres
 */
public class FfSpecialAttack implements TrueCloneable {
    private String id;
    private String text;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public FfSpecialAttack clone() throws CloneNotSupportedException {
        return (FfSpecialAttack) super.clone();
    }
}
