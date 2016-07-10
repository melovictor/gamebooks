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
        addArmyEntry("warriors", character.getWarriors());
        addArmyEntry("dwarves", character.getDwarves());
        addArmyEntry("elves", character.getElves());
        addArmyEntry("knights", character.getKnights());
        addArmyEntry("wilders", character.getWilders());
        addArmyEntry("northerns", character.getNortherns());
        addArmyEntry("marauders", character.getMarauders());
        addArmyEntry("whiteKnights", character.getWhiteKnights());
    }

    private void addArmyEntry(final String name, final int amount) {
        if (amount > 0) {
            army.add(new ArmyEntry(name, amount));
        }
    }

    public List<ArmyEntry> getArmy() {
        return army;
    }

}
