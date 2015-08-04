package hu.zagor.gamebooks.character.domain;

import hu.zagor.gamebooks.character.Character;

/**
 * Class for handling notes for the {@link Character}.
 * @author Tamas_Szekeres
 */
public class Note {

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

}
