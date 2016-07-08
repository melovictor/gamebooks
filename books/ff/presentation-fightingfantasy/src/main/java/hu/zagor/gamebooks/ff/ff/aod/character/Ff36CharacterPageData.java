package hu.zagor.gamebooks.ff.ff.aod.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data for FF36.
 * @author Tamas_Szekeres
 */
@Component("ff36CharacterPageData")
@Scope("prototype")
public class Ff36CharacterPageData extends FfCharacterPageData {
    private final List<ArmyEntry> army = new ArrayList<>();

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy ruleset.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff36CharacterPageData(final Ff36Character character, final FfCharacterHandler handler) {
        super(character, handler);

        addArmyEntry(character, "warriors", character.getWarriors());
        addArmyEntry(character, "dwarves", character.getDwarves());
        addArmyEntry(character, "elves", character.getElves());
        addArmyEntry(character, "knights", character.getKnights());
    }

    private void addArmyEntry(final Ff36Character character, final String name, final int amount) {
        if (amount > 0) {
            army.add(new ArmyEntry(name, character.getWarriors()));
        }
    }

    public List<ArmyEntry> getArmy() {
        return army;
    }

}
