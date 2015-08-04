package hu.zagor.gamebooks.renderer;

import hu.zagor.gamebooks.content.dice.DiceConfiguration;

/**
 * Interface for formatting dice images to display the thrown results nicely.
 * @author Tamas_Szekeres
 */
public interface DiceResultRenderer {

    /**
     * Renders the dice <span/> elements.
     * @param diceConfiguration the dice configuration used for the generation of the random numbers
     * @param results the generated numbers
     * @return the rendered <span/> elements
     */
    String render(DiceConfiguration diceConfiguration, int[] results);

    /**
     * Renders the dice <span/> elements.
     * @param diceSide the number of sides the dice has (for the css class)
     * @param results the generated numbers
     * @return the rendered <span/> elements
     */
    String render(int diceSide, int[] results);

}
