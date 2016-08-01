package hu.zagor.gamebooks.ff.ff.sob.mvc.books.section.service;

import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.sob.character.Ff16Character;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section post handler for FF16, section 48.
 * @author Tamas_Szekeres
 */
@Component
public class Ff16Section48PostHandler extends FfCustomPrePostSectionHandler {

    private static final int MAX_BET = 50;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final List<ProcessableItemHolder> itemsToProcess = wrapper.getParagraph().getItemsToProcess();
        if (!itemsToProcess.isEmpty()) {
            final Ff16Character character = (Ff16Character) wrapper.getCharacter();
            final int gold = character.getGold();
            final int maxBet = Math.min(gold, MAX_BET);
            final UserInputCommand command = (UserInputCommand) itemsToProcess.get(0).getCommand();
            command.setMax(maxBet);
        }
    }

}
