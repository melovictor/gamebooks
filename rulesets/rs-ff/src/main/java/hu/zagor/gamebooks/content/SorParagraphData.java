package hu.zagor.gamebooks.content;

import hu.zagor.gamebooks.content.choice.Choice;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;

/**
 * Extended paragraph data for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class SorParagraphData extends FfParagraphData {

    private List<Choice> spellChoices = new ArrayList<>();

    public List<Choice> getSpellChoices() {
        return spellChoices;
    }

    public void setSpellChoices(final List<Choice> spellChoices) {
        this.spellChoices = spellChoices;
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

}
