package hu.zagor.gamebooks.lw.mvc.book.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.domain.LwBookContentSpecification;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Default character generator for LW.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultLwCharacterGenerator implements CharacterGenerator {
    private static final int DICE_SIDE = 10;
    private static final int COMBAT_SKILL_DEFAULT = 10;
    private static final int ENDURANCE_DEFAULT = 20;

    @Autowired @Qualifier("d10") private RandomNumberGenerator rand;
    @Autowired private DiceResultRenderer diceRenderer;
    private Map<String, LwDisciplineMapper> disciplineMapper;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookContentSpecification bookContentSpecification) {
        return generateCharacter(characterObject, (LwBookContentSpecification) bookContentSpecification);
    }

    public Map<String, Object> generateCharacter(final Character characterObject, final LwBookContentSpecification bookContentSpecification) {
        Assert.notNull(characterObject, "The parameter 'characterObject' cannot be null!");
        Assert.notNull(bookContentSpecification, "The parameter 'bookContentSpecification' cannot be null!");
        final LwCharacter character = (LwCharacter) characterObject;
        final int[] endurance = rand.getRandomNumber(new DiceConfiguration(1, 0, 9), ENDURANCE_DEFAULT);
        final int[] combatSkill = rand.getRandomNumber(new DiceConfiguration(1, 0, 9), COMBAT_SKILL_DEFAULT);

        character.setBackpackSize(bookContentSpecification.getCharacterBackpackSize());
        character.setCombatSkill(combatSkill[0]);
        character.setEndurance(endurance[0]);
        character.setInitialEndurance(endurance[0]);

        character.setInitialized(true);

        final Map<String, Object> result = new HashMap<>();
        disciplineMapper.get(bookContentSpecification.getLevel()).mapDisciplines(character, result);

        result.put("lwEndurance", endurance[0] + diceRenderer.render(DICE_SIDE, endurance));
        result.put("lwCombatSkill", combatSkill[0] + diceRenderer.render(DICE_SIDE, combatSkill));
        result.put("lwEnduranceNumeric", endurance[0]);
        result.put("lwCombatSkillNumeric", combatSkill[0]);

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

    public void setDisciplineMapper(final Map<String, LwDisciplineMapper> disciplineMapper) {
        this.disciplineMapper = disciplineMapper;
    }

}
