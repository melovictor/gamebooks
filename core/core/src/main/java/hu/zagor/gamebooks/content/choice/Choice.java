package hu.zagor.gamebooks.content.choice;

import hu.zagor.gamebooks.content.EscapingData;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Bean for storing a single choice option with it's textual description, the target paragraph and an optional position in the list of choices.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Choice extends EscapingData implements Cloneable, Serializable {

    private final String id;
    private String display;
    private final String text;
    private final Integer position;
    private final String singleChoiceText;
    private boolean singleMode;

    /**
     * Default constructor for the xml deserializator.
     */
    Choice() {
        id = "";
        text = "";
        display = "";
        position = 0;
        singleChoiceText = "";
    }

    /**
     * Basic constructor.
     * @param id the id of the target choice, cannot be null
     * @param text the textual representation of the choice. If set to null, it will be replaced by a default text on the UI
     * @param position the absolute position of the choice in it's list, cannot be null
     * @param singleChoiceText alternate text that should be used in the main text block in place of a marker in case this is the only available choice left, while also replacing
     *        the text of the choice to the default value, or null if no such logic is to be used
     */
    public Choice(final String id, final String text, final Integer position, final String singleChoiceText) {
        Assert.notNull(id, "Parameter 'id' can not be null!");
        Assert.notNull(position, "Parameter 'position' can not be null!");
        this.id = id;
        display = id;
        this.text = fixText(text);
        this.position = position;
        this.singleChoiceText = fixText(singleChoiceText);
    }

    public String getId() {
        return id;
    }

    /**
     * Returns the specified section text (if one is specified), or null if the object is in single mode to trigger rendering the default next text on screen.
     * @return the text (which can be null), or null if the object is in single mode
     */
    public String getText() {
        return singleMode ? null : text;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public Choice clone() throws CloneNotSupportedException {
        return (Choice) super.clone();
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(final String display) {
        this.display = display;
    }

    /**
     * Gets the text that needs to be appended to the main text body, and if this text is specified, at the same time switches the object into single choice mode, meaning it will
     * report that it doesn't have a content text specified, triggering the default choice to be rendered on the screen.
     * @return the single choice text to be inserted into the main text block, can be null
     */
    public String getSingleChoiceText() {
        singleMode = singleChoiceText != null;
        return singleChoiceText;
    }

}
