package hu.zagor.gamebooks.ff.ff.ss.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF8.
 * @author Tamas_Szekeres
 */
@Component("ff8Character")
@Scope("prototype")
public class Ff8Character extends FfCharacter {
    private static final int INITIAL_SPELL_ITEMS = 6;

    private int spellItem = INITIAL_SPELL_ITEMS;
    private final Set<String> maps = new HashSet<>();

    /**
     * Default constructor initializing the map with the starting piece.
     */
    public Ff8Character() {
        maps.add("0");
    }

    public int getSpellItem() {
        return spellItem;
    }

    public void setSpellItem(final int spellItem) {
        this.spellItem = spellItem;
    }

    public Set<String> getMaps() {
        return maps;
    }
}
