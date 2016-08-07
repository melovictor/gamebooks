package hu.zagor.gamebooks.ff.ff.b.mvc.books.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.b.character.Ff60Character;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Custom character generator for FF60.
 * @author Tamas_Szekeres
 */
@Component
public class Ff60CharacterGenerator implements CharacterGenerator {
    private static final int GOLD_DEFAULT = 12;
    private static final int DICE_SIDE = 6;
    @Autowired @Qualifier("defaultFfCharacterGenerator") private CharacterGenerator decorated;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookInformations info, final Object generationInput) {
        final Map<String, Object> result = decorated.generateCharacter(characterObject, info, generationInput);

        final Ff60Character character = (Ff60Character) characterObject;
        final int[] gold = getRand().getRandomNumber(2, GOLD_DEFAULT);
        character.setGold(gold[0]);
        result.put("ffGold", gold[0] + getDiceRenderer().render(DICE_SIDE, gold));

        return result;
    }

    @Override
    public RandomNumberGenerator getRand() {
        return decorated.getRand();
    }

    @Override
    public DiceResultRenderer getDiceRenderer() {
        return decorated.getDiceRenderer();
    }

}
