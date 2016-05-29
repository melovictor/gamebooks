package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service;

import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Post section handler for ff14, section 377.
 * @author Tamas_Szekeres
 */
@Component
public class Ff14Section377PostHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final ChoiceSet choices = wrapper.getParagraph().getData().getChoices();
        if (choices.size() == 2) {
            choices.removeByPosition(1);
        }
    }

}
