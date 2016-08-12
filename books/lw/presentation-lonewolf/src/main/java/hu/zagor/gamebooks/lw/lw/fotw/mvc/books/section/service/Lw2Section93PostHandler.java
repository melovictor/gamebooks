package hu.zagor.gamebooks.lw.lw.fotw.mvc.books.section.service;

import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section handler.
 * @author Tamas_Szekeres
 */
@Component
public class Lw2Section93PostHandler implements CustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final List<ProcessableItemHolder> itemsToProcess = wrapper.getParagraph().getItemsToProcess();
        if (!itemsToProcess.isEmpty()) {
            final UserInputCommand command = (UserInputCommand) itemsToProcess.get(0).getCommand();
            final LwCharacter character = (LwCharacter) wrapper.getCharacter();
            final int totalGold = character.getMoney().getGoldCrowns();
            command.setMax(totalGold);
        }
    }

}
