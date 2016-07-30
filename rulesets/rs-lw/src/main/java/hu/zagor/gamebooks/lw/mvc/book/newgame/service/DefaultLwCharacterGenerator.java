package hu.zagor.gamebooks.lw.mvc.book.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.character.handler.item.LwCharacterItemHandler;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.LwBookContentSpecification;
import hu.zagor.gamebooks.domain.LwBookInformations;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.mvc.book.newgame.service.discipline.LwDisciplineMapper;
import hu.zagor.gamebooks.lw.mvc.book.newgame.service.equipment.LwEquipmentMapper;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

/**
 * Default character generator for LW.
 * @author Tamas_Szekeres
 */
public class DefaultLwCharacterGenerator implements CharacterGenerator {
    private static final int DICE_SIDE = 10;
    private static final int COMBAT_SKILL_DEFAULT = 10;
    private static final int ENDURANCE_DEFAULT = 20;

    @Autowired @Qualifier("d10") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer diceRenderer;
    private Map<String, LwDisciplineMapper> disciplineMapper;
    private final Map<Integer, LwEquipmentMapper> equipmentMapper = new HashMap<>();

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookContentSpecification bookContentSpecification, final BookInformations info) {
        return generateCharacter(characterObject, (LwBookContentSpecification) bookContentSpecification, (LwBookInformations) info);
    }

    private Map<String, Object> generateCharacter(final Character characterObject, final LwBookContentSpecification bookContentSpecification,
        final LwBookInformations info) {
        Assert.notNull(characterObject, "The parameter 'characterObject' cannot be null!");
        Assert.notNull(bookContentSpecification, "The parameter 'bookContentSpecification' cannot be null!");
        final LwCharacter character = (LwCharacter) characterObject;
        final DiceConfiguration d10Configuration = new DiceConfiguration(1, 0, 9);
        final int[] endurance = generator.getRandomNumber(d10Configuration, ENDURANCE_DEFAULT);
        final int[] combatSkill = generator.getRandomNumber(d10Configuration, COMBAT_SKILL_DEFAULT);

        character.setBackpackSize(bookContentSpecification.getCharacterBackpackSize());
        character.setCombatSkill(combatSkill[0]);
        character.setEndurance(endurance[0]);
        character.setInitialEndurance(endurance[0]);

        character.setInitialized(true);

        final Map<String, Object> result = new HashMap<>();
        disciplineMapper.get(bookContentSpecification.getLevel()).mapDisciplines(character, result);
        final int bookId = info.getPosition().intValue();
        final LwCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        itemHandler.addItem(characterObject, "40000", 1);
        equipmentMapper.get(bookId).mapEquipments(character, result, itemHandler);

        result.put("lwEndurance", endurance[0] + diceRenderer.render(DICE_SIDE, endurance));
        result.put("lwCombatSkill", combatSkill[0] + diceRenderer.render(DICE_SIDE, combatSkill));
        result.put("lwEnduranceNumeric", endurance[0]);
        result.put("lwCombatSkillNumeric", combatSkill[0]);

        return result;
    }

    /**
     * Adds a new equipment manager that can be used for a specific book.
     * @param bookId the number of the book for which the equipment manager is added
     * @param mapper the {@link LwEquipmentMapper} to be used
     */
    public void addEquipmentManager(final int bookId, final LwEquipmentMapper mapper) {
        equipmentMapper.put(bookId, mapper);
    }

    @Override
    public RandomNumberGenerator getRand() {
        return generator;
    }

    @Override
    public DiceResultRenderer getDiceRenderer() {
        return diceRenderer;
    }

    public void setDisciplineMapper(final Map<String, LwDisciplineMapper> disciplineMapper) {
        this.disciplineMapper = disciplineMapper;
    }

}
