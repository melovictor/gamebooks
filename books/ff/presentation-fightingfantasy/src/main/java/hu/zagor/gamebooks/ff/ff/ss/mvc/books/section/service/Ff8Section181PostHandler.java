package hu.zagor.gamebooks.ff.ff.ss.mvc.books.section.service;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Post section handler for section 181, FF8.
 * @author Tamas_Szekeres
 */
@Component
public class Ff8Section181PostHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final FfEnemy enemy = (FfEnemy) wrapper.getEnemies().get("9");
        if (enemy.getStamina() < enemy.getInitialStamina()) {
            enemy.setStamina(enemy.getStamina() + 1);
        }
    }

}
