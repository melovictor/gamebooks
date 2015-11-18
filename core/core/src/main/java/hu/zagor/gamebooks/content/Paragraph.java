package hu.zagor.gamebooks.content;

import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A very basic Paragraph that has an id and a data group containing further information.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Paragraph extends EscapingData implements TrueCloneable {

    private String id;
    private String displayId;
    private ParagraphData data;
    private int actions;

    private final List<String> validMoves;
    private final Map<String, Integer> validItems;

    @Autowired
    private transient ChoicePositionCounter positionCounter;

    Paragraph() {
        validMoves = new ArrayList<>();
        validItems = new HashMap<>();
    }

    /**
     * Basic constructor.
     * @param id the id of the paragraph, cannot be null
     * @param displayId the id of the paragraph to display; if null, the id will be displayed
     * @param actions the number of actions that can be executed by the player while staying at the specified paragraph
     */
    public Paragraph(final String id, final String displayId, final int actions) {
        this();
        Assert.notNull(id, "Parameter 'id' can not be null!");
        this.id = id;
        if (displayId == null) {
            this.displayId = id;
        } else {
            this.displayId = fixText(displayId);
        }
        this.actions = actions;
    }

    @Override
    public Paragraph clone() throws CloneNotSupportedException {
        final Paragraph cloned = (Paragraph) super.clone();
        cloned.data = data.clone();
        return cloned;
    }

    public String getId() {
        return id;
    }

    public ParagraphData getData() {
        return data;
    }

    public String getDisplayId() {
        return displayId;
    }

    /**
     * Collects and calculates valid steps from the current paragraph.
     */
    public void calculateValidEvents() {
        validItems.clear();
        validMoves.clear();
        data.calculateValidEvents(this);
    }

    /**
     * Check whether the given item id with the given amount is accessible from self.
     * @param glItem the id and amount of the item we want to gather
     * @return true, if the move is valid, false otherwise
     */
    public boolean isValidItem(final GatheredLostItem glItem) {
        final String itemId = glItem.getId();
        return validItems.containsKey(itemId) && validItems.get(itemId) >= glItem.getAmount();
    }

    void addValidItem(final String itemId, final int amount) {
        validItems.put(itemId, amount);
    }

    /**
     * Removes items from among the valid pack meaning these items will no longer be gatherable through the display list.
     * @param itemId the id of the item that has to be removed from the gatherable list, cannot be null
     * @param amount the amount of items to remove from the list, must be positive
     */
    public void removeValidItem(final String itemId, final int amount) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(amount > 0, "The parameter 'amount' must be positive!");

        if (validItems.containsKey(itemId)) {
            final int totalAmount = validItems.get(itemId);
            if (totalAmount <= amount) {
                validItems.remove(itemId);
            } else {
                validItems.put(itemId, totalAmount - amount);
            }
            data.removeValidItem(itemId, amount);
        }
    }

    Map<String, Integer> getValidItems() {
        return validItems;
    }

    /**
     * Check whether the given paragraph id is accessible from self.
     * @param paragraphId the id of the paragraph we want to move to
     * @return true, if the move is valid, false otherwise
     */
    public boolean isValidMove(final String paragraphId) {
        return validMoves.contains(paragraphId);
    }

    /**
     * Adds a new section id as a valid id to navigate from the current section.
     * @param paragraphId the id of the paragraph
     */
    public void addValidMove(final String paragraphId) {
        validMoves.add(paragraphId);
    }

    /**
     * Clears all currently selected valid moves.
     */
    public void clearValidMoves() {
        validMoves.clear();
    }

    public void setData(final ParagraphData data) {
        this.data = data;
    }

    public ChoicePositionCounter getPositionCounter() {
        return positionCounter;
    }

    public int getActions() {
        return actions;
    }

    public void setActions(final int actions) {
        this.actions = actions;
    }

}
