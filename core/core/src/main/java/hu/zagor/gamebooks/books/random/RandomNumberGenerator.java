package hu.zagor.gamebooks.books.random;

import hu.zagor.gamebooks.content.dice.DiceConfiguration;

/**
 * Interface for easily generating random numbers using 'x' pieces of 'y'-sided dice then adding 'z' to the result. Also usable for integration testing when we want predefined
 * values out of it.
 * @author Tamas_Szekeres
 */
public interface RandomNumberGenerator {

    /**
     * Generates a random dice roll.
     * @param dicePiece the number of dices
     * @param diceSide the sides of dices
     * @param addition the number to add
     * @param adding true, if the rolled numbers must be added together, false if they must be put one after the other
     * @return the array of thrown numbers from position 1 and the total on position 0
     */
    int[] getRandomNumber(int dicePiece, int diceSide, int addition, boolean adding);

    /**
     * Generates a random dice roll.
     * @param dicePiece the number of dices
     * @param diceSide the sides of dices
     * @param addition the number to add
     * @return the array of thrown numbers from position 1 and the total on position 0
     */
    int[] getRandomNumber(int dicePiece, int diceSide, int addition);

    /**
     * Generates a random dice roll.
     * @param dicePiece the number of dices
     * @param addition addition the number to add
     * @return the array of thrown numbers from position 1 and the total on position 0
     */
    int[] getRandomNumber(int dicePiece, int addition);

    /**
     * Generates a random dice roll using the default number of sides.
     * @param dicePiece the number of dices
     * @return the array of thrown numbers from position 1 and the total on position 0
     */
    int[] getRandomNumber(int dicePiece);

    /**
     * Generates a random dice roll using a dice configuration.
     * @param configuration the {@link DiceConfiguration} bean
     * @return the array of thrown numbers from position 1 and the total on position 0
     */
    int[] getRandomNumber(DiceConfiguration configuration);

    /**
     * Generates a random dice roll using a dice configuration.
     * @param configuration the {@link DiceConfiguration} bean
     * @param addition the number to add
     * @return the array of thrown numbers from position 1 and the total on position 0
     */
    int[] getRandomNumber(DiceConfiguration configuration, int addition);

    /**
     * Returns the side of the dice that is being thrown here when no dice side is explicitly specified.
     * @return the number of sides the default dice has
     */
    int getDefaultDiceSide();
}
