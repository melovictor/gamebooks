package hu.zagor.gamebooks.character.domain;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Class for handling notes for the {@link Character}.
 * @author Tamas_Szekeres
 */
public class Note implements TrueCloneable {

    private String note = "";

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return note;
    }

    @Override
    public Note clone() throws CloneNotSupportedException {
        return (Note) super.clone();
    }
}
