package hu.zagor.gamebooks.character;

import hu.zagor.gamebooks.character.domain.Note;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.CommandView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The player character.
 * @author Tamas_Szekeres
 */
public class Character implements Serializable {

    private final List<String> paragraphs;
    private final List<Item> equipment;
    private final List<Item> hiddenEquipment;
    private int backpackSize;

    private final Map<String, String> userInteraction;
    private CommandView commandView;
    private Note notes;

    /**
     * Basic constructor that expects an {@link ItemFactory} from which it can create new items for itself.
     */
    public Character() {
        paragraphs = new ArrayList<>();
        equipment = new ArrayList<>();
        userInteraction = new HashMap<>();
        hiddenEquipment = new ArrayList<>();
        notes = new Note();
    }

    public List<Item> getEquipment() {
        return equipment;
    }

    public CommandView getCommandView() {
        return commandView;
    }

    public void setCommandView(final CommandView commandView) {
        this.commandView = commandView;
    }

    public Note getNotes() {
        return notes;
    }

    /**
     * Sets the new notes for the {@link Character}.
     * @param notes the new notes
     */
    public void setNotes(final String notes) {
        if (this.notes == null) {
            this.notes = new Note();
        }
        this.notes.setNote(notes);
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public Map<String, String> getUserInteraction() {
        return userInteraction;
    }

    public List<Item> getHiddenEquipment() {
        return hiddenEquipment;
    }

    public int getBackpackSize() {
        return backpackSize;
    }

    public void setBackpackSize(final int backpackSize) {
        this.backpackSize = backpackSize;
    }

}
