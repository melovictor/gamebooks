package hu.zagor.gamebooks.books.random;

import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Default implementation of {@link RandomNumberGenerator}.
 * @author Tamas_Szekeres
 */
public class DefaultRandomNumberGenerator implements RandomNumberGenerator, ReplayingNumberGenerator {

    private final int defaultDiceSide;
    private final List<Integer> throwResultQueue = new ArrayList<>();
    private final List<Integer> thrownResults = new ArrayList<>();
    @Autowired
    private EnvironmentDetector environmentDetector;

    /**
     * Constructor to create a new instance of the generator with a default dice side.
     * @param defaultDiceSide the number of sides for the default dice
     */
    public DefaultRandomNumberGenerator(final int defaultDiceSide) {
        Assert.state(defaultDiceSide > 0, "The parameter 'defaultDiceSide' must be bigger than zero!");

        this.defaultDiceSide = defaultDiceSide;
    }

    @Override
    public int[] getRandomNumber(final int dicePiece, final int diceSide, final int addition) {
        Assert.isTrue(dicePiece > 0, "The parameter 'dicePiece' must be bigger than zero!");
        Assert.isTrue(diceSide > 0, "The parameter 'diceSide' must be bigger than zero!");
        final int[] results = new int[dicePiece + 1];

        results[0] = addition;

        for (int i = 0; i < dicePiece; i++) {
            results[i + 1] = getNextValue(diceSide);
            results[0] += results[i + 1];
        }

        return results;
    }

    @Override
    public int[] getRandomNumber(final int dicePiece, final int addition) {
        return getRandomNumber(dicePiece, defaultDiceSide, addition);
    }

    @Override
    public int[] getRandomNumber(final int dicePiece) {
        return getRandomNumber(dicePiece, defaultDiceSide, 0);
    }

    @Override
    public int[] getRandomNumber(final DiceConfiguration configuration) {
        return getRandomNumber(configuration, 0);
    }

    @Override
    public int[] getRandomNumber(final DiceConfiguration configuration, final int addition) {
        Assert.notNull(configuration, "The parameter 'configuration' cannot be null!");
        final int diceNumber = configuration.getDiceNumber();
        final int[] result = new int[diceNumber + 1];

        final int minValue = configuration.getMinValue();
        result[0] = addition;
        final int multiplier = configuration.getMaxValue() - minValue + 1;

        for (int i = 0; i < diceNumber; i++) {
            final int rnd = getNextValue(multiplier - 1) + minValue;
            result[i + 1] += rnd;
            result[0] += rnd;
        }

        return result;
    }

    private int getNextValue(final int diceSide) {
        int result;
        if (throwResultQueue.isEmpty() || !environmentDetector.isSeleniumTesting()) {
            result = (int) Math.floor(Math.random() * diceSide + 1);
            if (environmentDetector.isRecordState()) {
                thrownResults.add(result);
            }
        } else {
            result = throwResultQueue.remove(0);
        }
        return result;
    }

    @Override
    public int getDefaultDiceSide() {
        return defaultDiceSide;
    }

    @Override
    public void setUpThrowResultQueue(final List<Integer> results) {
        if (environmentDetector.isSeleniumTesting()) {
            throwResultQueue.clear();
            throwResultQueue.addAll(results);
        }
    }

    @Override
    public List<Integer> getThrownResults() {
        return thrownResults;
    }

}
