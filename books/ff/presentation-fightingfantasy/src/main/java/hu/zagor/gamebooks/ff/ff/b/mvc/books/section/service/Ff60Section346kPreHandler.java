package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.b.character.Ff60Character;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Pre section handler for ff60, section 346k.
 * @author Tamas_Szekeres
 */
@Component
public class Ff60Section346kPreHandler implements CustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final Ff60Character character = (Ff60Character) wrapper.getCharacter();
        character.setArrowScore(character.getArrowScore() * 2);
    }

}
