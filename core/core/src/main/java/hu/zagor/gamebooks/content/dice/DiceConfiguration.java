package hu.zagor.gamebooks.content.dice;

/**
 * Bean for carrying dice setup parameters.
 * @author Tamas_Szekeres
 */
public class DiceConfiguration {

    private final int diceNumber;
    private final int minValue;
    private final int maxValue;

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

}
