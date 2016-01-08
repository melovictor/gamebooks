package hu.zagor.gamebooks.ff.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for storing data to be displayed on the character page.
 * @author Tamas_Szekeres
 */
@Component("sorCharacterPageData")
@Scope("prototype")
public class SorCharacterPageData extends FfCharacterPageData {
    private final boolean usedLibra;
    private final List<Item> curses = new ArrayList<>();
    private final boolean commandActive;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy ruleset.
     * @param character the character
     * @param handler the {@link SorCharacter} to use for obtaining the characters' properties
     */
    public SorCharacterPageData(final SorCharacter character, final FfCharacterHandler handler) {
        super(character, handler);
        usedLibra = character.isUsedLibra();

        final Iterator<Item> itemIterator = getItems().iterator();
        while (itemIterator.hasNext()) {
            final Item item = itemIterator.next();
            if (item.getItemType() == ItemType.curseSickness) {
                curses.add(item);
                itemIterator.remove();
            }
        }
        commandActive = character.getCommandView() != null && character.getCommandView().getViewName().startsWith("sorFight");
    }

    public boolean isUsedLibra() {
        return usedLibra;
    }

    public List<Item> getCurses() {
        return curses;
    }

    public boolean isCommandActive() {
        return commandActive;
    }

}
