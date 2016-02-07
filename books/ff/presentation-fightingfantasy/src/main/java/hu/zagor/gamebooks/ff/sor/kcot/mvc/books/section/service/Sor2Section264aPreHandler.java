package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.service;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section 324 pre handler for Sorcery 2. Set haggling conditions with the Gnome.
 * @author Tamas_Szekeres
 */
@Component
public class Sor2Section264aPreHandler extends FfCustomPrePostSectionHandler {
    @Autowired private Sor2GnomeHagglingService gnomeHagglingService;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final Paragraph paragraph = wrapper.getParagraph();

        gnomeHagglingService.setHagglingCondition(paragraph, character);
    }

}
