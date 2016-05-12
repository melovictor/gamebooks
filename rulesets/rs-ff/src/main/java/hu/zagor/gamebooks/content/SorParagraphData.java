package hu.zagor.gamebooks.content;

import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;

/**
 * Extended paragraph data for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class SorParagraphData extends FfParagraphData {

    private List<Choice> spellChoices = new ArrayList<>();
    private List<ModifyAttribute> spellModifyAttributes = new ArrayList<>();

    @Override
    public SorParagraphData clone() throws CloneNotSupportedException {
        final SorParagraphData clone = (SorParagraphData) super.clone();

        clone.spellChoices = new ArrayList<>();
        for (final Choice choice : spellChoices) {
            clone.spellChoices.add(choice.clone());
        }
        clone.spellModifyAttributes = new ArrayList<>();
        for (final ModifyAttribute attribute : spellModifyAttributes) {
            clone.spellModifyAttributes.add(attribute.clone());
        }

        return clone;
    }

    public List<Choice> getSpellChoices() {
        return spellChoices;
    }

    /**
     * Determines whether this section is going to perform a spell jumping or not.
     * @return true if it's going to be a spelljump, false otherwise
     */
    public boolean isSpellJump() {
        final ChoiceSet choices = getChoices();
        String text = null;
        if (!choices.isEmpty()) {
            final Choice choice = choices.iterator().next();
            if (choice != null) {
                text = choice.getId();
            }
        }
        return text != null && text.startsWith("spellJump");
    }

    /**
     * Adds a new spell choice to the list of choices.
     * @param choice the new {@link Choice} object
     */
    public void addSpellChoice(final Choice choice) {
        spellChoices.add(choice);
    }

    /**
     * Adds new spell choices for the current data.
     * @param spellChoices the {@link Choice} objects to add, shouldn't be null
     */
    public void addSpellChoices(final List<Choice> spellChoices) {
        Assert.notNull(spellChoices, "The parameter 'spellChoices' cannot be null!");
        this.spellChoices.addAll(spellChoices);
    }

    public List<ModifyAttribute> getSpellModifyAttributes() {
        return spellModifyAttributes;
    }

    /**
     * Adds a new {@link ModifyAttribute} object to the paragraph that is a direct result of a spell being cast.
     * @param modifyAttribute the new object to add
     */
    public void addSpellModifyAttributes(final ModifyAttribute modifyAttribute) {
        spellModifyAttributes.add(modifyAttribute);
    }

    @Override
    public void calculateValidEvents(final Paragraph paragraph) {
        super.calculateValidEvents(paragraph);
        for (final Choice choice : spellChoices) {
            final String id = choice.getId();
            final String choiceText = choice.getText();
            getLogger().debug("Found spell choice #{}[@{}]: '{}'", id, String.valueOf(choice.getPosition()), choiceText == null ? "" : choiceText);
            paragraph.addValidMove(id);
        }
    }

}
