package hu.zagor.gamebooks.raw.section;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.CommandResolver;
import hu.zagor.gamebooks.content.commandlist.CommandListIterator;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.content.reward.Reward;
import hu.zagor.gamebooks.support.bookids.english.Series;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Implementation of the {@link BookParagraphResolver} for the raw ruleset.
 * @author Tamas_Szekeres
 */
public class RawRuleBookParagraphResolver implements BookParagraphResolver {

    @LogInject
    private Logger logger;
    @Autowired
    private ServerCommunicator communicator;

    @Override
    public void resolve(final ResolvationData resolvationData, final Paragraph paragraph) {
        Assert.notNull(paragraph, "The parameter 'paragraph' cannot be null!");
        Assert.notNull(resolvationData, "The parameter 'resolvationData' cannot be null!");

        doResolveData(resolvationData, paragraph.getData(), false, null);
    }

    private void doResolveData(final ResolvationData resolvationData, final ParagraphData subData, final boolean shouldExecuteBasics,
        final CommandListIterator mainCommandIterator) {
        if (shouldExecuteBasics) {
            executeBasics(resolvationData, subData);
        }
        resolveCommands(resolvationData, subData, mainCommandIterator);
        handleReward(resolvationData, subData);
    }

    private void handleReward(final ResolvationData resolvationData, final ParagraphData subData) {
        final Reward reward = subData.getReward();
        if (reward != null) {
            try {
                final String data = assemblePostData(resolvationData, reward);
                final URLConnection connection = communicator.connect("http://zagor.hu/recordreward.php");
                communicator.sendRequest(connection, data);
                communicator.receiveResponse(connection);
            } catch (final IOException exception) {
                logger.error("Failed to send reward data to server.", exception);
            }
        }
    }

    private String assemblePostData(final ResolvationData resolvationData, final Reward reward) throws IOException {
        String part = communicator.compilePostData("userId", resolvationData.getPlayerUser().getId(), null);
        final Long bookId = resolvationData.getInfo().getId();
        if (reward.isSeriesId()) {
            final long seriesId = bookId - bookId % Series.SERIES_MULTIPLIER;
            part = communicator.compilePostData("bookId", seriesId, part);
        } else {
            part = communicator.compilePostData("bookId", bookId, part);
        }
        return communicator.compilePostData("rewardId", reward.getId(), part);
    }

    /**
     * Executes the more complicated commands in the ruleset.
     * @param resolvationData the resolvationData containing all relevant objects for the resolvation
     * @param subData the currently processed paragraph data
     * @param mainCommandIterator the main command iterator to which we must add the commands from the current set, or null if this is the root {@link ParagraphData} we're
     *        resolving
     */
    protected void resolveCommands(final ResolvationData resolvationData, final ParagraphData subData, final CommandListIterator mainCommandIterator) {
        final Character character = resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();

        final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
        gatherItems(subData, character, itemHandler);
        loseItems(subData, character, itemHandler);
        handleHideUnhide(character.getEquipment(), character.getHiddenEquipment(), subData.getHiddenItems());
        handleHideUnhide(character.getHiddenEquipment(), character.getEquipment(), subData.getUnhiddenItems());

        if (mainCommandIterator == null) {
            executeCommands(resolvationData, subData.getCommands().forwardsIterator(), character);
        } else {
            prepareCommandsForExecutionOnRoot(subData, mainCommandIterator);
        }
    }

    private void handleHideUnhide(final List<Item> sourceStore, final List<Item> targetStore, final List<GatheredLostItem> itemsToMove) {
        for (final GatheredLostItem glItem : itemsToMove) {
            for (int i = 0; i < glItem.getAmount(); i++) {
                final Item item = getItem(sourceStore, glItem.getId());
                if (item != null) {
                    sourceStore.remove(item);
                    targetStore.add(item);
                }
            }
        }
    }

    private Item getItem(final List<Item> sourceStore, final String id) {
        Item selectedItem = null;
        for (final Item item : sourceStore) {
            if (id.equals(item.getId())) {
                selectedItem = item;
            }
        }
        return selectedItem;
    }

    /**
     * Method to handle losing items.
     * @param subData the {@link ParagraphData} with the item list
     * @param character the {@link Character} losign the item
     * @param itemHandler the {@link CharacterItemHandler} object
     */
    protected void loseItems(final ParagraphData subData, final Character character, final CharacterItemHandler itemHandler) {
        for (final GatheredLostItem item : subData.getLostItems()) {
            logger.debug("Lost item {}", item.getId());
            itemHandler.removeItem(character, item.getId(), item.getAmount());
        }
        subData.getLostItems().clear();
    }

    private void gatherItems(final ParagraphData subData, final Character character, final CharacterItemHandler itemHandler) {
        for (final GatheredLostItem item : subData.getGatheredItems()) {
            logger.debug("Gathered item {}", item.getId());
            itemHandler.addItem(character, item.getId(), item.getAmount());
        }
        subData.getGatheredItems().clear();
    }

    private void executeCommands(final ResolvationData resolvationData, final CommandListIterator commandIterator, final Character character) {
        while (commandIterator.hasNext()) {
            final Command command = commandIterator.next();
            logger.debug("Executing command {}.", command.toString());

            final CommandResolver resolver = resolvationData.getInfo().getCommandResolvers().get(command.getClass());

            final CommandResolveResult resolve = resolver.resolve(command, resolvationData);
            if (!resolve.isFinished()) {
                character.setCommandView(command.getCommandView(getRulesetPrefix()));
            } else {
                character.setCommandView(null);
            }
            final List<ParagraphData> resolveList = resolve.getResolveList();
            if (resolveList != null) {
                for (final ParagraphData resolvableSubData : resolve.getResolveList()) {
                    if (resolvableSubData != null) {
                        doResolveData(resolvationData, resolvableSubData, true, commandIterator);
                    }
                }
            }
            if (!resolve.isFinished()) {
                break;
            }
            commandIterator.remove();
        }
    }

    private void prepareCommandsForExecutionOnRoot(final ParagraphData subData, final CommandListIterator mainCommandIterator) {
        mainCommandIterator.add(subData.getCommands());
    }

    /**
     * Returns the current ruleset's identification code to use as a prefix for command view resolvations.
     * @return the prefix
     */
    protected String getRulesetPrefix() {
        return "raw";
    }

    /**
     * Executes the most basic actions available for the ruleset.
     * @param resolvationData the resolvationData containing all relevant objects for the resolvation
     * @param subData the currently processed paragraph data
     */
    protected void executeBasics(final ResolvationData resolvationData, final ParagraphData subData) {
        final ParagraphData rootDataElement = resolvationData.getRootData();
        rootDataElement.appendText(subData.getText());
        rootDataElement.addChoices(subData.getChoices());
    }

    public Logger getLogger() {
        return logger;
    }

}
