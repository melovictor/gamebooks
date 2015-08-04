package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.content.EscapingData;
import hu.zagor.gamebooks.content.TrueCloneable;

import java.util.Map;

/**
 * Abstract main bean for storing commands like fight order, item checking, changes in properties, etc.
 * @author Tamas_Szekeres
 */
public abstract class Command extends EscapingData implements TrueCloneable {

    @Override
    public Command clone() throws CloneNotSupportedException {
        return (Command) super.clone();
    }

    /**
     * This method returns the {@link CommandView} object that contains all necessary information for displaying the view properly where the user interaction can occur.
     * @param rulesetPrefix the prefix of the ruleset to choose between the different rulesets' views for the same basic command
     * @return the {@link CommandView} object
     * @throws UnsupportedOperationException when the {@link Command} doesn't support {@link CommandView}
     */
    public CommandView getCommandView(final String rulesetPrefix) {
        throw new UnsupportedOperationException("This object doesn't support command view. Your view with prefix " + rulesetPrefix + " will not be used.");
    }

    /**
     * Return a valid move for this specific command.
     * @return the valid move, or null if this particular command doesn't have a user-specific component
     */
    public abstract String getValidMove();

    /**
     * Clones any {@link TrueCloneable} objects.
     * @param cloneable the object to clone
     * @return the cloned object
     * @throws CloneNotSupportedException if there is a problem while cloning
     * @param <T> the exact type of the object, determined automatically
     */
    @SuppressWarnings("unchecked")
    protected <T extends TrueCloneable> T cloneObject(final T cloneable) throws CloneNotSupportedException {
        T cloned = null;
        if (cloneable != null) {
            cloned = (T) cloneable.clone();
        }
        return cloned;
    }

    /**
     * Makes sure that the choices are not visible.
     * @param model the {@link Map} to be populated by the hiding information
     */
    protected void hideChoices(final Map<String, Object> model) {
        model.put("ffChoiceHidden", true);
    }

}
