package hu.zagor.gamebooks.lw.mvc.book.newgame.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterContinuator;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.handler.item.LwCharacterItemHandler;
import hu.zagor.gamebooks.lw.domain.LwBookContentSpecification;
import hu.zagor.gamebooks.lw.mvc.book.newgame.domain.LwCharGenInput;
import hu.zagor.gamebooks.lw.mvc.book.newgame.service.discipline.LwDisciplineMapper;
import hu.zagor.gamebooks.lw.mvc.book.newgame.service.equipment.LwEquipmentMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

/**
 * Default character continuator for LW.
 * @author Tamas_Szekeres
 */
public class DefaultLwCharacterContinuator implements CharacterContinuator {
    private Map<String, LwDisciplineMapper> disciplineMapper;
    private final Map<Integer, LwEquipmentMapper> equipmentMapper = new HashMap<>();

    @Override
    public Map<String, Object> continueCharacter(final Character characterObject, final BookInformations info, final Object continuationInput) {
        Assert.notNull(info, "The parameter 'info' cannot be null!");
        Assert.notNull(characterObject, "The parameter 'characterObject' cannot be null!");
        final LwBookContentSpecification bookContentSpecification = (LwBookContentSpecification) info.getContentSpecification();
        Assert.notNull(bookContentSpecification, "The parameter 'bookContentSpecification' cannot be null!");

        final Map<String, Object> result = new HashMap<>();

        final LwCharGenInput input = (LwCharGenInput) continuationInput;
        final LwCharacter character = (LwCharacter) characterObject;

        disciplineMapper.get(bookContentSpecification.getLevel()).mapDisciplines(character, result, input);
        final int bookId = info.getPosition().intValue();
        final LwCharacterItemHandler itemHandler = (LwCharacterItemHandler) info.getCharacterHandler().getItemHandler();
        equipmentMapper.get(bookId).mapEquipments(character, result, itemHandler, input);

        return result;
    }

    public void setDisciplineMapper(final Map<String, LwDisciplineMapper> disciplineMapper) {
        this.disciplineMapper = disciplineMapper;
    }

    /**
     * Adds a new equipment manager that can be used for a specific book.
     * @param bookId the number of the book for which the equipment manager is added
     * @param mapper the {@link LwEquipmentMapper} to be used
     */
    public void addEquipmentManager(final int bookId, final LwEquipmentMapper mapper) {
        equipmentMapper.put(bookId, mapper);
    }

}
