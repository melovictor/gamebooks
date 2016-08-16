package hu.zagor.gamebooks.character.handler.character;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.domain.BookInformations;
import java.util.Map;

/**
 * Interface for continuing with a generic character.
 * @author Tamas_Szekeres
 */
public interface CharacterContinuator {
    /**
     * Fills all of the properties of a character based on the book-specific rules.
     * @param characterObject the {@link Character} to fill
     * @param info the {@link BookInformations} object
     * @param continuationInput book- or ruleset-specific object containing user input for the continuation, can be null
     * @return detailed information about the continuation
     */
    Map<String, Object> continueCharacter(Character characterObject, BookInformations info, Object continuationInput);

}
