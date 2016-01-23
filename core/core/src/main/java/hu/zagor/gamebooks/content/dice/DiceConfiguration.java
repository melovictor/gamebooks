package hu.zagor.gamebooks.content.dice;

/**
 * Bean for carrying dice setup parameters.
 * @author Tamas_Szekeres
 */
public class DiceConfiguration {

    private final int diceNumber;
    private final int minValue;
    private final int maxValue;
    private final boolean adding;

    /**
     * Basic constructor.
     * @param diceNumber the number of dices
     * @param minValue the minimum value a dice can throw
     * @param maxValue the maximum value a dice can throw
     */
    public DiceConfiguration(final int diceNumber, final int minValue, final int maxValue) {
        this.diceNumber = diceNumber;
        this.minValue = minValue;
        this.maxValue = maxValue;
        adding = true;
    }

    /**
     * Basic constructor with formatting pattern.
     * @param diceNumber the number of dices
     * @param minValue the minimum value a dice can throw
     * @param maxValue the maximum value a dice can throw
     * @param adding true, if the rolled numbers must be added together, false if they must be put one after the other
     */
    public DiceConfiguration(final int diceNumber, final int minValue, final int maxValue, final boolean adding) {
        this.diceNumber = diceNumber;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.adding = adding;
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public boolean isAdding() {
        return adding;
    }

}
