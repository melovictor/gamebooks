package hu.zagor.gamebooks.ff.ff.tot.character;

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
 * Character page data object for FF14.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff14CharacterPageData extends FfCharacterPageData {
    private final List<Item> spells = new ArrayList<>();

    /**
     * Basic constructor to filter all data that has to be displayed on the character page.
     * @param character the {@link FfCharacter} object
     * @param handler the {@link FfCharacterHandler} object
     */
    public Ff14CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);

        final Iterator<Item> itemIterator = handler.getItemHandler().getItemIterator(character);
        while (itemIterator.hasNext()) {
            final Item item = itemIterator.next();
            if (item.getId().startsWith("41")) {
                spells.add(item);
            }
        }
    }

    public List<Item> getSpells() {
        return spells;
    }

}
