package hu.zagor.gamebooks.raw.character;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.Note;
import hu.zagor.gamebooks.character.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for storing data to display on the character page on the page.
 * @author Tamas_Szekeres
 */
@Component("rawCharacterPageData")
@Scope("prototype")
public class RawCharacterPageData {

    private final List<String> info;
    private final List<String> equipment;
    private final Note notes;

    /**
     * Basic constructor that accepts a character.
     * @param character the {@link Character}
     */
    public RawCharacterPageData(final Character character) {
        final List<Item> equipments = character.getEquipment();
        this.info = new ArrayList<>();
        this.equipment = new ArrayList<>();
        notes = character.getNotes();

        for (final Item item : equipments) {
            switch (item.getItemType()) {
            case common:
                equipment.add(item.getName());
                break;
            case info:
                info.add(item.getName());
                break;
            default:
            }
        }

        Collections.sort(this.info);
        Collections.sort(this.equipment);
    }

    public List<String> getInfo() {
        return info;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public Note getNotes() {
        return notes;
    }

}
