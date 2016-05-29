package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Pre section handler for FF14, section 13.
 * @author Tamas_Szekeres
 */
@Component
public class Ff14Section13PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final FfEnemy enemy = (FfEnemy) wrapper.getEnemies().get("29");
        final Character character = wrapper.getCharacter();
        final FfAttributeHandler attributeHandler = info.getCharacterHandler().getAttributeHandler();
        enemy.setFleeAtRound(attributeHandler.resolveValue(character, "skill"));
    }

}
