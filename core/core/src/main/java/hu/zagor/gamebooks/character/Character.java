package hu.zagor.gamebooks.character;

import hu.zagor.gamebooks.character.domain.Note;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.TrueCloneable;
import hu.zagor.gamebooks.content.command.CommandView;
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
public class Character implements TrueCloneable {

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

    @Override
    public Character clone() throws CloneNotSupportedException {
        final Character cloned = (Character) super.clone();

        cloned.equipment = cloneItems(equipment);
        cloned.hiddenEquipment = cloneItems(hiddenEquipment);
        cloned.notes = notes.clone();
        cloned.codeWords = new HashSet<>(codeWords);
        cloned.paragraphs = new ArrayList<>(paragraphs);
        cloned.userInteraction = new HashMap<>(userInteraction);

        return cloned;
    }

    private List<Item> cloneItems(final List<Item> itemList) {
        final List<Item> items = new ArrayList<>();
        for (final Item item : itemList) {
            items.add(item.clone());
        }
        return items;
    }
}
