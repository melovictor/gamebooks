package hu.zagor.gamebooks.ff.ff.sos.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF34.
 * @author Tamas_Szekeres
 */
@Component("ff34CharacterPageData")
@Scope("prototype")
public class Ff34CharacterPageData extends FfCharacterPageData {
    private final List<Item> spells = new ArrayList<>();

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy ruleset.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff34CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);
        filterSpells();
    }

    private void filterSpells() {
        filterByIterator(getProvisions().iterator());
        filterByIterator(getItems().iterator());
    }

    private void filterByIterator(final Iterator<Item> iterator) {
        while (iterator.hasNext()) {
            final Item item = iterator.next();
            if (item.getId().startsWith("41")) {
                iterator.remove();
                spells.add(item);
            }
        }
    }

    public List<Item> getSpells() {
        return spells;
    }

}
