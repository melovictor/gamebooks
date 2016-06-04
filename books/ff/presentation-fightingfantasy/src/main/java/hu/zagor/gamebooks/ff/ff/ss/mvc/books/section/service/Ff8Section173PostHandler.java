package hu.zagor.gamebooks.ff.ff.ss.mvc.books.section.service;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.ss.character.Ff8Character;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Post section handler for section 173, FF8.
 * @author Tamas_Szekeres
 */
@Component
public class Ff8Section173PostHandler implements CustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final Ff8Character character = (Ff8Character) wrapper.getCharacter();
        character.setSpellItem(character.getSpellItem() - 1);
    }

}
