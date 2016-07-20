package hu.zagor.gamebooks.lw.mvc.book.newgame.service.equipment;

import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import java.util.Map;

public interface LwEquipmentMapper {

    void mapEquipments(LwCharacter character, Map<String, Object> result, CharacterItemHandler itemHandler);

}
