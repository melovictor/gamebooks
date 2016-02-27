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
import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.CommandResolver;
import hu.zagor.gamebooks.content.commandlist.CommandList;
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

    @LogInject private Logger logger;
    @Autowired private ServerCommunicator communicator;

    @Override
    public void resolve(final ResolvationData resolvationData, final Paragraph paragraph) {
        Assert.notNull(paragraph, "The parameter 'paragraph' cannot be null!");
        Assert.notNull(resolvationData, "The parameter 'resolvationData' cannot be null!");

        final List<ProcessableItemHolder> itemsToProcess = paragraph.getItemsToProcess();
        boolean startBasicExecutionImmediately = true;
        if (itemsToProcess.isEmpty()) {
            itemsToProcess.add(new ProcessableItemHolder(paragraph.getData()));
            startBasicExecutionImmediately = false;
        }

        resolvationData.getCharacter().setCommandView(null);
        doResolveData(resolvationData, paragraph, startBasicExecutionImmediately);
    }

    private void doResolveData(final ResolvationData resolvationData, final Paragraph paragraph, final boolean startBasicExecutionImmediately) {
        boolean doBasicExecution = startBasicExecutionImmediately;
        final List<ProcessableItemHolder> itemsToProcess = paragraph.getItemsToProcess();
        final Character character = resolvationData.getCharacter();

        while (!itemsToProcess.isEmpty() && character.getCommandView() == null) {
            final ProcessableItemHolder processableItemHolder = itemsToProcess.get(0);
            if (processableItemHolder.isParagraphDataHolder()) {
                final ParagraphData paragraphData = processableItemHolder.getParagraphData();
                if (doBasicExecution) {
                    executeBasics(resolvationData, paragraphData);
                } else {
                    doBasicExecution = true;
                }
                resolveBasicCommands(resolvationData, paragraphData);
                addAll(itemsToProcess, paragraphData.getCommands());
                handleReward(resolvationData, paragraphData);
                itemsToProcess.remove(processableItemHolder);
            } else {
                final Command command = processableItemHolder.getCommand();
                final CommandResolveResult resolveResult = resolveComplexCommands(resolvationData, paragraph, command, character);
                if (resolveResult.isFinished()) {
                    itemsToProcess.remove(processableItemHolder);
                }
            }

        }
    }

    private void addAll(final List<ProcessableItemHolder> itemsToProcess, final CommandList commands) {
        int idx = 0;
        for (final Command command : commands) {
            if (command != null) {
                itemsToProcess.add(idx++, new ProcessableItemHolder(command));
            }
        }
    }

    private void addAll(final List<ProcessableItemHolder> itemsToProcess, final List<ParagraphData> paragraphs) {
        if (paragraphs != null) {
            int idx = 0;
            for (final ParagraphData data : paragraphs) {
                if (data != null) {
                    itemsToProcess.add(idx++, new ProcessableItemHolder(data));
                }
            }
        }
    }

    private void handleReward(final ResolvationData resolvationData, final ParagraphData subData) {
        final Reward reward = subData.getReward();
        if (reward != null) {
            try {
                logger.info("Fetching reward '{}' for book '{}' for player '{}'.", reward.getId(), resolvationData.getInfo().getTitle(),
                    resolvationData.getPlayerUser().getPrincipal());
                final String data = assemblePostData(resolvationData, reward);
                final URLConnection connection = communicator.connect("http://zagor.hu/recordreward.php");
                communicator.sendRequest(connection, data);
                communicator.receiveResponse(connection);
                final Long bookId = resolvationData.getInfo().getId();
                resolvationData.getPlayerUser().addReward(getRelevantId(reward, bookId), reward.getId());
            } catch (final IOException exception) {
                logger.error("Failed to send reward data to server.", exception);
            }
        }
    }

    private String assemblePostData(final ResolvationData resolvationData, final Reward reward) throws IOException {
        String part = communicator.compilePostData("userId", resolvationData.getPlayerUser().getId(), null);
        final Long bookId = resolvationData.getInfo().getId();
        final long id = getRelevantId(reward, bookId);
        part = communicator.compilePostData("bookId", id, part);
        return communicator.compilePostData("rewardId", reward.getId(), part);
    }

    private long getRelevantId(final Reward reward, final long bookId) {
        long id;

        if (reward.isSeriesId()) {
            id = bookId - bookId % Series.SERIES_MULTIPLIER;
        } else {
            id = bookId;
        }

        return id;
    }

    /**
     * Executes the more complicated commands in the ruleset.
     * @param resolvationData the resolvationData containing all relevant objects for the resolvation
     * @param subData the currently processed paragraph data
     */
    protected void resolveBasicCommands(final ResolvationData resolvationData, final ParagraphData subData) {
        final Character character = resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();

        final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
        handleHide(character, subData.getHiddenItems(), itemHandler);
        handleUnhide(character, subData.getUnhiddenItems());
        gatherItems(subData, character, itemHandler);
        loseItems(subData, character, itemHandler);
    }

    private void handleHide(final Character character, final List<GatheredLostItem> itemsToMove, final CharacterItemHandler itemHandler) {
        for (final GatheredLostItem glItem : itemsToMove) {
            final List<Item> removedItems = itemHandler.removeItem(character, glItem);
            character.getHiddenEquipment().addAll(removedItems);
        }
    }

    private void handleUnhide(final Character character, final List<GatheredLostItem> itemsToMove) {
        final List<Item> sourceStore = character.getHiddenEquipment();
        final List<Item> targetStore = character.getEquipment();
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
            if (item != null && id.equals(item.getId())) {
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

    private CommandResolveResult resolveComplexCommands(final ResolvationData resolvationData, final Paragraph paragraph, final Command command,
        final Character character) {
        logger.debug("Executing command {}.", command.toString());

        final CommandResolver resolver = resolvationData.getInfo().getCommandResolvers().get(command.getClass());

        final CommandResolveResult resolve = resolver.resolve(command, resolvationData);
        if (!resolve.isFinished()) {
            character.setCommandView(command.getCommandView(getRulesetPrefix()));
        } else {
            character.setCommandView(null);
        }
        addAll(paragraph.getItemsToProcess(), resolve.getResolveList());
        return resolve;
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
        final ParagraphData rootDataElement = resolvationData.getParagraph().getData();
        rootDataElement.appendText(subData.getText());
        rootDataElement.addChoices(subData.getChoices());
    }

    public Logger getLogger() {
        return logger;
    }

}
