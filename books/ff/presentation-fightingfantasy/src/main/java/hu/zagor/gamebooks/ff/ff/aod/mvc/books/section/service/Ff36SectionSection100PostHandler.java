package hu.zagor.gamebooks.ff.ff.aod.mvc.books.section.service;

import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.aod.character.Ff36Character;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section 100 post handler for FF36.
 * @author Tamas_Szekeres
 */
@Component
public class Ff36SectionSection100PostHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final UserInputCommand command = (UserInputCommand) wrapper.getParagraph().getItemsToProcess().get(0).getCommand();
        final Ff36Character character = (Ff36Character) wrapper.getCharacter();
        command.setMax(Math.min(command.getMax(), character.getGold()));
    }

}
