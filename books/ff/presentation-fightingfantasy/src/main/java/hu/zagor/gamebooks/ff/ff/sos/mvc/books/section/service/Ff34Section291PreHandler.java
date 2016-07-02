package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service;

import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section 291 pre handler for FF34.
 * @author Tamas_Szekeres
 */
@Component
public class Ff34Section291PreHandler extends FfCustomPrePostSectionHandler {

    private static final int MAX_BETTABLE_GOLD_PIECES = 6;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final int currentGold = info.getCharacterHandler().getAttributeHandler().resolveValue(wrapper.getCharacter(), "gold");
        final int maxBet = Math.min(currentGold, MAX_BETTABLE_GOLD_PIECES);
        final UserInputCommand command = (UserInputCommand) wrapper.getParagraph().getData().getCommands().get(1);
        command.setMax(maxBet);
    }

}
