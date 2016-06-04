package hu.zagor.gamebooks.ff.ff.ss.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data for FF8.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff8CharacterPageData extends FfCharacterPageData {
    private final int spellItem;
    private final List<FfItem> spells;

    /**
     * Basic constructor.
     * @param character the {@link FfCharacter} object
     * @param handler the {@link FfCharacterHandler} object
     */
    public Ff8CharacterPageData(final Ff8Character character, final FfCharacterHandler handler) {
        super(character, handler);
        spellItem = character.getSpellItem();
        spells = new ArrayList<>();
        fillSpells(getPotions());
        fillSpells(getItems());
    }

    private void fillSpells(final List<Item> equipment) {
        final Iterator<Item> iterator = equipment.iterator();
        while (iterator.hasNext()) {
            final Item item = iterator.next();
            if (item.getId().startsWith("31")) {
                iterator.remove();
                spells.add((FfItem) item);
            }
        }
    }

    public int getSpellItem() {
        return spellItem;
    }

    public List<FfItem> getSpells() {
        return spells;
    }

}
