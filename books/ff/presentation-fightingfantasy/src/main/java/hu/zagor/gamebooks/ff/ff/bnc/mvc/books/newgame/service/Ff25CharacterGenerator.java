package hu.zagor.gamebooks.ff.ff.bnc.mvc.books.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.bnc.character.Ff25Character;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Character generator for FF25.
 * @author Tamas_Szekeres
 */
public class Ff25CharacterGenerator implements CharacterGenerator {
    private static final int DICE_SIDE = 6;
    private static final int WILLPOWER_DEFAULT = 6;

    @Autowired @Qualifier("defaultFfCharacterGenerator") private CharacterGenerator superGenerator;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookInformations info, final Object generationInput) {
        final Map<String, Object> generateCharacter = superGenerator.generateCharacter(characterObject, info, generationInput);

        final Ff25Character character = (Ff25Character) characterObject;
        final int[] willpower = superGenerator.getRand().getRandomNumber(1, WILLPOWER_DEFAULT);

        character.setWillpower(willpower[0]);
        character.setInitialWillpower(willpower[0]);

        generateCharacter.put("ffWillpower", character.getInitialWillpower() + superGenerator.getDiceRenderer().render(DICE_SIDE, willpower));

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
