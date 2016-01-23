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

    private static final int DECIMAL_MUTLIPLIER = 10;
    private final int defaultDiceSide;
    private final List<Integer> throwResultQueue = new ArrayList<>();
    private final List<Integer> thrownResults = new ArrayList<>();
    @Autowired private EnvironmentDetector environmentDetector;

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
        return getRandomNumber(dicePiece, diceSide, addition, true);
    }

    @Override
    public int[] getRandomNumber(final int dicePiece, final int diceSide, final int addition, final boolean adding) {
        Assert.isTrue(dicePiece > 0, "The parameter 'dicePiece' must be bigger than zero!");
        Assert.isTrue(diceSide > 0, "The parameter 'diceSide' must be bigger than zero!");
        final int[] results = new int[dicePiece + 1];

        if (adding) {
            results[0] = addition;
            for (int i = 0; i < dicePiece; i++) {
                results[i + 1] = getNextValue(diceSide);
                results[0] += results[i + 1];
            }
        } else {
            results[0] = 0;
            for (int i = 0; i < dicePiece; i++) {
                results[i + 1] = getNextValue(diceSide) + addition;
                results[0] = results[0] * DECIMAL_MUTLIPLIER + results[i + 1];
            }
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
        final int diceSide = configuration.getMaxValue() - configuration.getMinValue() + 1;
        final int actualAddition = addition + configuration.getMinValue() - 1;
        final int[] rolledNumbers = getRandomNumber(configuration.getDiceNumber(), diceSide, actualAddition, configuration.isAdding());
        if (!configuration.isAdding()) {
            if (rolledNumbers[0] == 0) {
                rolledNumbers[0] = (int) Math.pow(diceSide, configuration.getDiceNumber());
            }
        }

        return rolledNumbers;
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
