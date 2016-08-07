package hu.zagor.gamebooks.lw.mvc.book.newgame.service.equipment;

import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.handler.item.LwCharacterItemHandler;
import hu.zagor.gamebooks.lw.mvc.book.newgame.domain.LwCharGenInput;
import java.util.Map;

/**
 * Interface for classes with which the equipment can be initialized when a new book is started.
 * @author Tamas_Szekeres
 */
public interface LwEquipmentMapper {

    /**
     * Executes the actual mapping.
     * @param character the {@link LwCharacter} into which the items must be mapped
     * @param result the map containing information to be sent back to the browser with results
     * @param itemHandler the {@link LwCharacterItemHandler} object
     * @param input bean containing the user input
     */
    void mapEquipments(LwCharacter character, Map<String, Object> result, LwCharacterItemHandler itemHandler, LwCharGenInput input);

}
