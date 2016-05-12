package hu.zagor.gamebooks.character;

import hu.zagor.gamebooks.character.domain.Note;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.CommandView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The player character.
 * @author Tamas_Szekeres
 */
public class Character implements Serializable {

    private List<String> paragraphs;
    private List<Item> equipment;
    private List<Item> hiddenEquipment;
    private Set<String> codeWords;
    private int backpackSize;

    private Map<String, String> userInteraction;
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
        codeWords = new HashSet<>();
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

    public Set<String> getCodeWords() {
        return codeWords;
    }

    public void setParagraphs(final List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public void setEquipment(final List<Item> equipment) {
        this.equipment = equipment;
    }

    public void setHiddenEquipment(final List<Item> hiddenEquipment) {
        this.hiddenEquipment = hiddenEquipment;
    }

    public void setCodeWords(final Set<String> codeWords) {
        this.codeWords = codeWords;
    }

    public void setNotes(final Note notes) {
        this.notes = notes;
    }

    public void setUserInteraction(final Map<String, String> userInteraction) {
        this.userInteraction = userInteraction;
    }

}
