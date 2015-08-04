package hu.zagor.gamebooks.recording;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;

/**
 * Abstract class for recording a specific event into the users' notes.
 * @author Tamas_Szekeres
 */
public abstract class AbstractPlaybackEventRecorder {

    /**
     * Adds a specific piece of string to the notes.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param string the text to append
     */
    protected void add(final HttpSessionWrapper wrapper, final String string) {
        final Character character = wrapper.getCharacter();
        String notes = character.getNotes().getNote();
        notes += string;
        character.setNotes(notes);
    }

    /**
     * Adds a specific piece of string inside a {@link StringBuilder} to the notes.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param builder the {@link StringBuilder} containing the text to append
     */
    protected void add(final HttpSessionWrapper wrapper, final StringBuilder builder) {
        final Character character = wrapper.getCharacter();
        String notes = character.getNotes().getNote();
        notes += builder.toString();
        character.setNotes(notes);
    }

}
