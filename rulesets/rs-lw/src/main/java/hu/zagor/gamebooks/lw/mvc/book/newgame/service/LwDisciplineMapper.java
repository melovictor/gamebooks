package hu.zagor.gamebooks.lw.mvc.book.newgame.service;

import hu.zagor.gamebooks.lw.character.LwCharacter;
import java.util.Map;

public interface LwDisciplineMapper {

    void mapDisciplines(LwCharacter character, Map<String, Object> result);

}
