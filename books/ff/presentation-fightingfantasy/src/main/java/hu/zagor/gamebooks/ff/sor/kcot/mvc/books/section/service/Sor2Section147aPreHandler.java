package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.service;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section 147a pre handler for Sorcery 2. Put number of arrows we have into the text.
 * @author Tamas_Szekeres
 */
@Component
public class Sor2Section147aPreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
        final Paragraph paragraph = wrapper.getParagraph();

        final String text = paragraph.getData().getText();
        final String arrowsLeft = String.valueOf(itemHandler.getItems(character, "4036").size());
        paragraph.getData().setText(text.replace("XX", arrowsLeft));
    }

}
