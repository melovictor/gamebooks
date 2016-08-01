package hu.zagor.gamebooks.ff.ff.sob.mvc.books.section.service;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.sob.character.Ff16Character;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Post section handler for FF16, section 171.
 * @author Tamas_Szekeres
 */
@Component
public class Ff16Section171PostHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final Paragraph paragraph = wrapper.getParagraph();
        if (paragraph.getItemsToProcess().isEmpty()) {
            final ParagraphData data = paragraph.getData();
            final Ff16Character character = (Ff16Character) wrapper.getCharacter();
            data.getChoices().removeByPosition(character.getTime() % 2);
        }
    }

}
