package hu.zagor.gamebooks.ff.ff.sob.mvc.books.newgame.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.sob.character.Ff16Character;
import hu.zagor.gamebooks.ff.mvc.book.newgame.service.DefaultFfCharacterGenerator;
import java.util.Map;

/**
 * Character generator for FF16.
 * @author Tamas_Szekeres
 */
public class Ff16CharacterGenerator extends DefaultFfCharacterGenerator {

    private static final int CREW_STRIKE_BASE = 6;
    private static final int CREW_STRENGTH_BASE = 6;
    private static final int DICE_SIDE = 6;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookContentSpecification bookContentSpecification, final BookInformations info) {
        final Map<String, Object> generateCharacter = super.generateCharacter(characterObject, bookContentSpecification, info);

        final Ff16Character character = (Ff16Character) characterObject;

        final int[] crewStrike = getRand().getRandomNumber(1);
        final int[] crewStrength = getRand().getRandomNumber(2);

        character.setCrewStrike(crewStrike[0] + CREW_STRIKE_BASE);
        character.setCrewStrength(crewStrength[0] + CREW_STRENGTH_BASE);
        character.setInitialCrewStrike(crewStrike[0] + CREW_STRIKE_BASE);
        character.setInitialCrewStrength(crewStrength[0] + CREW_STRENGTH_BASE);

        generateCharacter.put("ffCrewStrike", character.getCrewStrike() + getDiceRenderer().render(DICE_SIDE, crewStrike));
        generateCharacter.put("ffCrewStrength", character.getCrewStrength() + getDiceRenderer().render(DICE_SIDE, crewStrength));

        return generateCharacter;
    }
}
