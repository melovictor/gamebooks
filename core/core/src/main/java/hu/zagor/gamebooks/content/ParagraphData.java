package hu.zagor.gamebooks.content;

import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.content.reward.Reward;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;

/**
 * A very basic bean for describing a generic paragraph that contains a block of text, possible choices, items and commands.
 * @author Tamas_Szekeres
 */
public class ParagraphData extends EscapingData implements TrueCloneable, BeanFactoryAware {

    private static final Pattern TAKEN_ITEM_PATTERN = Pattern
        .compile("(<span(?=[^>]+class=\"(?:take|replace|purchase)Item\")(?=[^>]+data-id=\"([^\"]+)\")(?=[^>]+data-amount=\"(\\d+)\")[^>]+>)([^<]+)</span>");

    private static final int LINK_OPENING_TAG_GROUP = 1;
    private static final int ITEM_ID_GROUP = 2;
    private static final int ITEM_AMOUNT_GROUP = 3;
    private static final int CONTAINED_TEXT_GROUP = 4;

    private static final String DATA_AMOUNT_FORMAT = "data-amount=\"%d\"";

    @LogInject private Logger logger;
    private transient ChoicePositionCounter positionCounter;
    private String text = "";
    private ChoiceSet choices;
    private CommandList commands = new CommandList();
    private List<GatheredLostItem> gatheredItems = new ArrayList<>();
    private List<GatheredLostItem> lostItems = new ArrayList<>();
    private List<GatheredLostItem> hiddenItems = new ArrayList<>();
    private List<GatheredLostItem> unhiddenItems = new ArrayList<>();
    private Reward reward;

    private BeanFactory beanFactory;

    /**
     * Collects and calculates valid events (like next steps, gatherable items, etc) from the current paragraph.
     * @param paragraph the root {@link Paragraph} element actually collecting the valid events, cannot be null
     */
    public void calculateValidEvents(final Paragraph paragraph) {
        Assert.notNull(paragraph, "The parameter 'data' cannot be null!");

        gatherValidMoves(paragraph);
        gatherValidItems(paragraph);
        gatherValidUserResponses(paragraph);
    }

    private void gatherValidUserResponses(final Paragraph paragraph) {
        for (final Command command : getCommands()) {
            final String move = command.getValidMove();
            if (move != null) {
                logger.debug("Found command '{}'.", move);
                paragraph.addValidMove(move);
            }
        }
    }

    private void gatherValidItems(final Paragraph paragraph) {
        final Matcher matcher = TAKEN_ITEM_PATTERN.matcher(text);
        while (matcher.find()) {
            final String itemId = matcher.group(ITEM_ID_GROUP);
            final int amount = Integer.parseInt(matcher.group(ITEM_AMOUNT_GROUP));
            logger.debug("Found item {}.", itemId);
            paragraph.addValidItem(itemId, amount);
        }
    }

    private void gatherValidMoves(final Paragraph paragraph) {
        for (final Choice choice : choices) {
            final String id = choice.getId();
            final String choiceText = choice.getText();
            logger.debug("Found choice #{}[@{}]: '{}'", id, String.valueOf(choice.getPosition()), choiceText == null ? "" : choiceText);
            paragraph.addValidMove(id);
        }
    }

    /**
     * Removes the referencing for the given item in the given amount from the text.
     * @param itemId the id of the item that is no longer valid, cannot be null
     * @param amount the amount that has to be subtracted, must be positive
     */
    public void removeValidItem(final String itemId, final int amount) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(amount > 0, "The parameter 'amount' must be positive!");

        final Matcher matcher = TAKEN_ITEM_PATTERN.matcher(text);
        while (matcher.find()) {
            if (itemId.equals(matcher.group(ITEM_ID_GROUP))) {
                final int maxAmount = Integer.parseInt(matcher.group(ITEM_AMOUNT_GROUP));
                if (maxAmount <= amount) {
                    text = text.replace(matcher.group(), matcher.group(CONTAINED_TEXT_GROUP));
                } else {
                    final String fullTakeItemLink = matcher.group(LINK_OPENING_TAG_GROUP);
                    final String amendedTakeItemLink = fullTakeItemLink.replace(String.format(DATA_AMOUNT_FORMAT, maxAmount),
                        String.format(DATA_AMOUNT_FORMAT, maxAmount - amount));
                    text = text.replace(fullTakeItemLink, amendedTakeItemLink);
                }
            }
        }
    }

    public CommandList getCommands() {
        return commands;
    }

    /**
     * Adds a new command for the current data.
     * @param command the {@link Command} bean to add, cannot be null
     */
    public void addCommand(final Command command) {
        Assert.notNull(command, "The parameter 'command' cannot be null!");
        commands.add(command);
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = fixText(text);
    }

    /**
     * Appends text to the current one if it's not null.
     * @param text the {@link String} to append, can be null in which case no text will be added.
     */
    public void appendText(final String text) {
        if (text != null) {
            this.text += fixText(text);
        }
    }

    public ChoiceSet getChoices() {
        return choices;
    }

    /**
     * Adds a new choice for the current data.
     * @param choice the {@link Choice} bean to add, cannot be null
     */
    public void addChoice(final Choice choice) {
        Assert.notNull(choice, "Parameter 'choice' can not be null!");
        choices.add(choice);
    }

    /**
     * Adds new choices for the current data.
     * @param choices the {@link Choice} beans to add, cannot be null
     */
    public void addChoices(final Set<Choice> choices) {
        Assert.notNull(choices, "Parameter 'choices' can not be null!");
        this.choices.addAll(choices);
    }

    @Override
    public ParagraphData clone() throws CloneNotSupportedException {
        final ParagraphData cloned = (ParagraphData) super.clone();
        cloned.choices = beanFactory.getBean(ChoiceSet.class);
        for (final Choice choice : choices) {
            cloned.addChoice(choice.clone());
        }
        cloned.commands = new CommandList();
        for (final Command command : commands) {
            cloned.commands.add(command.clone());
        }
        cloned.gatheredItems = cloneGliList(gatheredItems);
        cloned.lostItems = cloneGliList(lostItems);
        cloned.hiddenItems = cloneGliList(hiddenItems);
        cloned.unhiddenItems = cloneGliList(unhiddenItems);

        return cloned;
    }

    private List<GatheredLostItem> cloneGliList(final List<GatheredLostItem> original) throws CloneNotSupportedException {
        final List<GatheredLostItem> cloned = new ArrayList<>();
        for (final GatheredLostItem item : original) {
            cloned.add(item.clone());
        }
        return cloned;
    }

    public List<GatheredLostItem> getGatheredItems() {
        return gatheredItems;
    }

    public List<GatheredLostItem> getLostItems() {
        return lostItems;
    }

    /**
     * Adds a new {@link GatheredLostItem} bean to the list of items gathered.
     * @param item the gathered item, cannot be null
     */
    public void addGatheredItem(final GatheredLostItem item) {
        Assert.notNull(item, "The parameter 'item' cannot be null!");
        gatheredItems.add(item);
    }

    /**
     * Adds a new {@link GatheredLostItem} bean to the list of items lost.
     * @param item the lost item, cannot be null
     */
    public void addLostItem(final GatheredLostItem item) {
        Assert.notNull(item, "The parameter 'item' cannot be null!");
        lostItems.add(item);
    }

    public ChoicePositionCounter getPositionCounter() {
        return positionCounter;
    }

    public void setPositionCounter(final ChoicePositionCounter positionCounter) {
        this.positionCounter = positionCounter;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setChoices(final ChoiceSet choices) {
        this.choices = choices;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(final Reward reward) {
        this.reward = reward;
    }

    public List<GatheredLostItem> getHiddenItems() {
        return hiddenItems;
    }

    public List<GatheredLostItem> getUnhiddenItems() {
        return unhiddenItems;
    }

    /**
     * Adds a new {@link GatheredLostItem} bean to the list of items to hide.
     * @param item the item to hide, cannot be null
     */
    public void addHiddenItem(final GatheredLostItem item) {
        Assert.notNull(item, "The parameter 'item' cannot be null!");
        hiddenItems.add(item);
    }

    /**
     * Adds a new {@link GatheredLostItem} bean to the list of items unhidden.
     * @param item the item to unhide, cannot be null
     */
    public void addUnhiddenItem(final GatheredLostItem item) {
        Assert.notNull(item, "The parameter 'item' cannot be null!");
        unhiddenItems.add(item);
    }
}
