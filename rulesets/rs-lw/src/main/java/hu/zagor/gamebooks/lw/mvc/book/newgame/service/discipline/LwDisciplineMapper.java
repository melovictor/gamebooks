package hu.zagor.gamebooks.lw.mvc.book.newgame.service.discipline;

import hu.zagor.gamebooks.lw.character.LwCharacter;
import java.util.Map;

/**
 * Interface for classes with which the disciplines can be initialized when a new book is started.
 * @author Tamas_Szekeres
 */
public interface LwDisciplineMapper {
    /**
     * Executes the actual mapping.
     * @param character the {@link LwCharacter} into which the items must be mapped
     * @param result the map containing information to be sent back to the browser with results
     */
    void mapDisciplines(LwCharacter character, Map<String, Object> result);

}
