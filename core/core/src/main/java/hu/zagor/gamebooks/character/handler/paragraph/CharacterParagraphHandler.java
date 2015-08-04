package hu.zagor.gamebooks.character.handler.paragraph;

import hu.zagor.gamebooks.character.Character;

/**
 * Class for handling paragraph-related operations on the {@link Character}.
 * @author Tamas_Szekeres
 */
public interface CharacterParagraphHandler {

    /**
     * Adds the given paragraph to the character's path as visited.
     * @param paragraph the paragraph that has been visited by the character, cannot be null
     * @param character the {@link Character} on which the addition has to be executed
     */
    void addParagraph(final Character character, final String paragraph);

    /**
     * Checks whether the given paragraph has been visited by the current character.
     * @param paragraph the paragraph we wish to check for previous visitation, cannot be null
     * @param character the {@link Character} on which the check has to be executed
     * @return true if the given paragraph has already been visited, false otherwise
     */
    boolean visitedParagraph(final Character character, final String paragraph);
}
