package hu.zagor.gamebooks.ff.pt.isotll.mvc.books.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.pt.isftll.character.Pt14Character;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Character generator for PT14.
 * @author Tamas_Szekeres
 */
public class Pt14CharacterGenerator implements CharacterGenerator {

    private static final int DICE_SIDE = 6;

    @Autowired @Qualifier("defaultFfCharacterGenerator") private CharacterGenerator superGenerator;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookContentSpecification bookContentSpecification, final BookInformations info) {
        final Map<String, Object> generateCharacter = superGenerator.generateCharacter(characterObject, bookContentSpecification, info);

        final Pt14Character character = (Pt14Character) characterObject;
        final int[] wisdom = superGenerator.getRand().getRandomNumber(1, DICE_SIDE, 0);

        character.setWisdom((wisdom[0] + 1) / 2);

        generateCharacter.put("ffWisdom", character.getWisdom() + superGenerator.getDiceRenderer().render(DICE_SIDE, wisdom));

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
