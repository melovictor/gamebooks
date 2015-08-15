package hu.zagor.gamebooks.ff.ff.tcoc.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF2.
 * @author Tamas_Szekeres
 */
@Component("ff2CharacterPageData")
@Scope("prototype")
public class Ff2CharacterPageData extends FfCharacterPageData {

    private final List<Item> spells = new ArrayList<>();

    @Resource(name = "ff2SpellIds")
    private List<String> spellIds;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 10 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff2CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);
    }

    /**
     * Initializer method to fix the equipment list with the spells.
     */
    @PostConstruct
    public void init() {
        final Iterator<Item> itemIterator = getItems().iterator();
        while (itemIterator.hasNext()) {
            final Item item = itemIterator.next();
            if (spellIds.contains(item.getId())) {
                itemIterator.remove();
                spells.add(item);
            }
        }
    }

    public List<Item> getSpells() {
        return spells;
    }

}
