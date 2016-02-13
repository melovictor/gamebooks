package hu.zagor.gamebooks.ff.mvc.book.section.service;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.attributetest.SuccessFailureDataContainer;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.itemcheck.CheckType;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

/**
 * Service for adding the magic chain handling logic to the system.
 * @author Tamas_Szekeres
 */
@Controller
public class SorMagicChainPreparatorService {
    @Autowired private MessageSource messageSource;
    @Autowired private LocaleProvider localeProvider;

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
        final FfParagraphData failedData = createFailedTestData();
        final SuccessFailureDataContainer failedLuckTest = new SuccessFailureDataContainer(failedData, null);

        final AttributeTestCommand secondLuckTest = createSecondLuckTest(failedLuckTest);

        final FfParagraphData successFirstData = new SorParagraphData();
        successFirstData.addCommand(secondLuckTest);

        final SuccessFailureDataContainer successfulFirstLuckTest = new SuccessFailureDataContainer(successFirstData, null);

        final AttributeTestCommand firstLuckTest = createAttributeTest(failedLuckTest, successfulFirstLuckTest);

        final ItemCheckCommand checkItemCommand = createItemCheckCommand(firstLuckTest);

        final FfParagraphData data = new SorParagraphData();
        data.addCommand(checkItemCommand);

        return data;
    }

    private ItemCheckCommand createItemCheckCommand(final AttributeTestCommand firstLuckTest) {
        final ParagraphData usedMagicChain = new SorParagraphData();
        usedMagicChain.setText(resolveText("page.sor.magicChain.canRetrieve"));
        usedMagicChain.addCommand(firstLuckTest);

        final ItemCheckCommand checkItemCommand = new ItemCheckCommand();
        checkItemCommand.setCheckType(CheckType.item);
        checkItemCommand.setId("3044");
        checkItemCommand.setAmount(1);
        checkItemCommand.setDontHave(usedMagicChain);
        return checkItemCommand;
    }

    private AttributeTestCommand createSecondLuckTest(final SuccessFailureDataContainer failedLuckTest) {
        final FfParagraphData successSecondData = createSecondSuccessData();
        final SuccessFailureDataContainer successfulSecondLuckTest = new SuccessFailureDataContainer(successSecondData, null);

        final AttributeTestCommand secondLuckTest = new AttributeTestCommand();
        secondLuckTest.setAgainst("luck");
        secondLuckTest.setConfigurationName("dice2d6");
        secondLuckTest.getFailure().add(failedLuckTest);
        secondLuckTest.getSuccess().add(successfulSecondLuckTest);
        return secondLuckTest;
    }

    private AttributeTestCommand createAttributeTest(final SuccessFailureDataContainer failedLuckTest, final SuccessFailureDataContainer successfulFirstLuckTest) {
        final AttributeTestCommand firstLuckTest = new AttributeTestCommand();
        firstLuckTest.setAgainst("luck");
        firstLuckTest.setConfigurationName("dice2d6");
        firstLuckTest.setLabel(resolveText("page.sor.magicChain.firstLuckTestLabel"));
        firstLuckTest.setCanSkip(true);
        firstLuckTest.setSkipText(resolveText("page.sor.magicChain.abandonChain"));
        firstLuckTest.getSuccess().add(successfulFirstLuckTest);
        firstLuckTest.getFailure().add(failedLuckTest);
        return firstLuckTest;
    }

    private FfParagraphData createSecondSuccessData() {
        final FfParagraphData successSecondData = new SorParagraphData();
        successSecondData.setText(resolveText("page.sor.magicChain.chainRetrievalSucceeded"));
        final GatheredLostItem glItem = new GatheredLostItem("3044", 1, 0, false);
        successSecondData.getGatheredItems().add(glItem);
        return successSecondData;
    }

    private FfParagraphData createFailedTestData() {
        final FfParagraphData failedData = new SorParagraphData();
        failedData.setText(resolveText("page.sor.magicChain.chainRetrievalFailed"));
        return failedData;
    }

    private String resolveText(final String textKey) {
        return messageSource.getMessage(textKey, null, localeProvider.getLocale());
    }
}
