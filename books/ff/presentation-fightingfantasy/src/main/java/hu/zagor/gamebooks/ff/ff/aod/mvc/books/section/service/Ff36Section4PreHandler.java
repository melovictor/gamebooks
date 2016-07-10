package hu.zagor.gamebooks.ff.ff.aod.mvc.books.section.service;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.aod.character.Ff36Character;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Pre handler for section 4, FF36.
 * @author Tamas_Szekeres
 */
@Component
public class Ff36Section4PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        if (wrapper.getParagraph().getItemsToProcess().size() == 2) {
            final Ff36Character character = (Ff36Character) wrapper.getCharacter();
            final int totalElves = character.getElves() * 2;
            final FfEnemy enemy = (FfEnemy) wrapper.getEnemies().get("107");
            enemy.setStamina(enemy.getStamina() - totalElves);
        }
    }

}
