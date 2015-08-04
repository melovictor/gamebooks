package hu.zagor.gamebooks.ff.mvc.book.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link FfCharacterGenerator} interface.
 * @author Tamas_Szekeres
 */
public class DefaultFfCharacterGenerator implements CharacterGenerator {

    private static final int DICE_SIDE = 6;
    private static final int SKILL_DEFAULT = 6;
    private static final int STAMINA_DEFAULT = 12;
    private static final int LUCK_DEFAULT = 6;

    @Autowired
    @Qualifier("d6RandomGenerator")
    private RandomNumberGenerator rand;
    @Autowired
    private DiceResultRenderer diceRenderer;

    /**
     * Fills the default properties of a character based on the standard rules.
     * @param characterObject the {@link FfCharacter} to fill
     * @return details about the generation process
     */
    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookContentSpecification bookContentSpecification) {
        Assert.notNull(characterObject, "The parameter 'characterObject' cannot be null!");
        Assert.notNull(bookContentSpecification, "The parameter 'bookContentSpecification' cannot be null!");
        final FfCharacter character = (FfCharacter) characterObject;
        final int[] skill = rand.getRandomNumber(1, SKILL_DEFAULT);
        final int[] stamina = rand.getRandomNumber(2, STAMINA_DEFAULT);
        final int[] luck = rand.getRandomNumber(1, LUCK_DEFAULT);

        character.setBackpackSize(bookContentSpecification.getCharacterBackpackSize());
        character.setSkill(skill[0]);
        character.setInitialSkill(skill[0]);
        character.setStamina(stamina[0]);
        character.setInitialStamina(stamina[0]);
        character.setLuck(luck[0]);
        character.setInitialLuck(luck[0]);

        character.setInitialized(true);

        final Map<String, Object> result = new HashMap<>();
        result.put("ffSkill", skill[0] + diceRenderer.render(DICE_SIDE, skill));
        result.put("ffStamina", stamina[0] + diceRenderer.render(DICE_SIDE, stamina));
        result.put("ffLuck", luck[0] + diceRenderer.render(DICE_SIDE, luck));
        result.put("ffSkillNumeric", skill[0]);
        result.put("ffStaminaNumeric", stamina[0]);
        result.put("ffLuckNumeric", luck[0]);

        return result;
    }

    @Override
    public RandomNumberGenerator getRand() {
        return rand;
    }

    @Override
    public DiceResultRenderer getDiceRenderer() {
        return diceRenderer;
    }

}
