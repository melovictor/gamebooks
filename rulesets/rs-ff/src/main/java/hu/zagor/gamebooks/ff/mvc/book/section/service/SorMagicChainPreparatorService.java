package hu.zagor.gamebooks.ff.mvc.book.section.service;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import java.util.List;
import org.springframework.stereotype.Controller;

/**
 * Service for adding the magic chain handling logic to the system.
 * @author Tamas_Szekeres
 */
@Controller
public class SorMagicChainPreparatorService {

    /**
     * Preparates the currently active {@link FightCommand} object to handle the retrieval of the chain.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param info the {@link FfBookInformations} object
     */
    public void preparateIfNeeded(final HttpSessionWrapper wrapper, final FfBookInformations info) {
        if (hasMagicChain(wrapper, info)) {
            if (fightNeedsToBePreparated(wrapper) && fightWinNotPreparatedYet(wrapper, info)) {
                final FightCommand command = extractFightCommand(wrapper);

                final FfParagraphData newWinData = getNewWinData();

                final List<FightOutcome> win = command.getWin();
                if (win.isEmpty()) {
                    final FightOutcome outcome = new FightOutcome();
                    outcome.setParagraphData(newWinData);
                    win.add(outcome);
                } else {
                    for (final FightOutcome outcome : win) {
                        final FfParagraphData currentWinData = outcome.getParagraphData();
                        final FfParagraphData selfWinData = getWinDataClone(newWinData);
                        outcome.setParagraphData(selfWinData);
                        final ItemCheckCommand itemCheckCommand = (ItemCheckCommand) selfWinData.getCommands().get(0);
                        itemCheckCommand.setAfter(currentWinData);
                    }
                }
                markFightAsPreparated(wrapper, info);
            }
        }
    }

    private boolean fightNeedsToBePreparated(final HttpSessionWrapper wrapper) {
        final FightCommand command = extractFightCommand(wrapper);
        return !"ally".equals(command.getResolver());
    }

    private FfParagraphData getWinDataClone(final FfParagraphData newWinData) {
        FfParagraphData clone;
        try {
            clone = newWinData.clone();
        } catch (final CloneNotSupportedException e) {
            clone = null;
        }
        return clone;
    }

    private FightCommand extractFightCommand(final HttpSessionWrapper wrapper) {
        return (FightCommand) wrapper.getParagraph().getItemsToProcess().get(0).getCommand();
    }

    private void markFightAsPreparated(final HttpSessionWrapper wrapper, final FfBookInformations info) {
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfUserInteractionHandler interactionHandler = characterHandler.getInteractionHandler();
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final Paragraph paragraph = wrapper.getParagraph();
        interactionHandler.setFightCommand(character, "preparatedWin" + paragraph.getId(), "done");
    }

    private boolean fightWinNotPreparatedYet(final HttpSessionWrapper wrapper, final FfBookInformations info) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfUserInteractionHandler interactionHandler = characterHandler.getInteractionHandler();
        final Paragraph paragraph = wrapper.getParagraph();

        return interactionHandler.peekLastFightCommand(character, "preparatedWin" + paragraph.getId()) == null;
    }

    private boolean hasMagicChain(final HttpSessionWrapper wrapper, final FfBookInformations info) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
        return itemHandler.hasItem(character, "3044");
    }

    private FfParagraphData getNewWinData() {
        final FfParagraphData data = new FfParagraphData();

        final ItemCheckCommand command = new ItemCheckCommand();
        command.setId("3044");
        final ParagraphData usedMagicChain = new ParagraphData();
        usedMagicChain.setText("you can get back your magic chain if you want to");
        command.setDontHave(usedMagicChain);
        data.addCommand(command);

        return data;
    }
}
