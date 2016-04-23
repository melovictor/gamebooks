package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section pre-handler for Ff60, section 161.
 * @author Tamas_Szekeres
 */
@Component
public class Ff60Section161PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final FfEnemy enemy = (FfEnemy) wrapper.getEnemies().get("11");
        final int skill = info.getCharacterHandler().getAttributeHandler().resolveValue(wrapper.getCharacter(), "skill");
        enemy.setFleeAtRound(skill);
    }

}
