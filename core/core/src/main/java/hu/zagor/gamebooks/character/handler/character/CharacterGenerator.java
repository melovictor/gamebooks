package hu.zagor.gamebooks.character.handler.character;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Map;

/**
 * Interface for generating a generic character.
 * @author Tamas_Szekeres
 */
public interface CharacterGenerator {

    /**
     * Fills all of the properties of a character based on the book-specific rules.
     * @param characterObject the {@link Character} to fill
     * @param info the {@link BookInformations} object
     * @param generationInput book- or ruleset-specific object containing user input for the generation, can be null
     * @return detailed information about the generation
     */
    Map<String, Object> generateCharacter(Character characterObject, BookInformations info, Object generationInput);

    /**
     * Gives out the random number generator.
     * @return the {@link RandomNumberGenerator} object
     */
    RandomNumberGenerator getRand();

    /**
     * Gives out the dice renderer.
     * @return the {@link DiceResultRenderer} object
     */
    DiceResultRenderer getDiceRenderer();
}
