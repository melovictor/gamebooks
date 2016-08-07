package hu.zagor.gamebooks.ff.ff.votv.mvc.books.newgame.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.votv.character.Ff38Character;
import hu.zagor.gamebooks.ff.mvc.book.newgame.service.DefaultFfCharacterGenerator;
import java.util.Map;

/**
 * Character generator for FF10.
 * @author Tamas_Szekeres
 */
public class Ff38CharacterGenerator extends DefaultFfCharacterGenerator {

    private static final int DICE_SIDE = 6;
    private static final int FAITH_DEFAULT = 3;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookInformations info, final Object generationInput) {
        final Map<String, Object> generateCharacter = super.generateCharacter(characterObject, info, generationInput);

        final Ff38Character character = (Ff38Character) characterObject;
        final int[] faith = getRand().getRandomNumber(1, FAITH_DEFAULT);

        character.setFaith(faith[0]);

        generateCharacter.put("ffFaith", faith[0] + getDiceRenderer().render(DICE_SIDE, faith));

        return generateCharacter;
    }
}
