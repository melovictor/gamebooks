package hu.zagor.gamebooks.ff.ff.hoh.mvc.books.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.ff.ff.hoh.character.Ff10Character;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Character generator for FF10.
 * @author Tamas_Szekeres
 */
public class Ff10CharacterGenerator implements CharacterGenerator {

    private static final int DICE_SIDE = 6;
    private static final int FEAR_DEFAULT = 6;

    @Autowired
    @Qualifier("defaultFfCharacterGenerator")
    private CharacterGenerator superGenerator;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookContentSpecification bookContentSpecification) {
        final Map<String, Object> generateCharacter = superGenerator.generateCharacter(characterObject, bookContentSpecification);

        final Ff10Character character = (Ff10Character) characterObject;
        final int[] fear = superGenerator.getRand().getRandomNumber(1, FEAR_DEFAULT);

        character.setFear(0);
        character.setInitialFear(fear[0]);

        generateCharacter.put("ffFear", character.getInitialFear() + superGenerator.getDiceRenderer().render(DICE_SIDE, fear));

        return generateCharacter;
    }

    @Override
    public RandomNumberGenerator getRand() {
        return superGenerator.getRand();
    }

    @Override
    public DiceResultRenderer getDiceRenderer() {
        return superGenerator.getDiceRenderer();
    }
}
