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
 * Post section handler.
 * @author Tamas_Szekeres
 */
@Component
public class Lw2Section238PostHandler implements CustomPrePostSectionHandler {

    private static final String CARTWHEEL_WINNINGS_KEY = "cartwheelWon";
    private static final int MAX_WINNABLE = 40;
    private static final int FULL_WIN_MULTIPLIER = 8;
    private static final int PENDING_COMMAND_COUNT = 4;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final List<ProcessableItemHolder> itemsToProcess = wrapper.getParagraph().getItemsToProcess();
        if (itemsToProcess.size() == PENDING_COMMAND_COUNT) {
            setMaxBet(wrapper, itemsToProcess);
        }
    }

    private void setMaxBet(final HttpSessionWrapper wrapper, final List<ProcessableItemHolder> itemsToProcess) {
        final UserInputCommand command = (UserInputCommand) itemsToProcess.get(1).getCommand();
        final LwCharacter character = (LwCharacter) wrapper.getCharacter();
        command.setMax(getMaxBetValue(character, character.getMoney().getGoldCrowns()));
    }

    private int getMaxBetValue(final LwCharacter character, final int maxGc) {
        final int totalWin = getCurrentWinnings(character);
        return Math.min((MAX_WINNABLE - totalWin) / FULL_WIN_MULTIPLIER, maxGc);
    }

    private int getCurrentWinnings(final LwCharacter character) {
        String winningsString = character.getUserInteraction().get(CARTWHEEL_WINNINGS_KEY);
        if (winningsString == null) {
            winningsString = "0";
        }
        return Integer.parseInt(winningsString);
    }

}
