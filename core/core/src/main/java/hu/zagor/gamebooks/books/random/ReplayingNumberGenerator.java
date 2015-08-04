package hu.zagor.gamebooks.books.random;

import java.util.List;

/**
 * Interface for a number generator that is capable to spit back a set of predefined values or to record a list of numbers generated randomly.
 * @author Tamas_Szekeres
 */
public interface ReplayingNumberGenerator {

    /**
     * Sets a test-related throwing queue, only usable during testing mode.
     * @param results the thrown results that has to be returned to the user
     */
    void setUpThrowResultQueue(final List<Integer> results);

    /**
     * Returns the currently recorded list of values thrown.
     * @return the list of previously thrown values
     */
    List<Integer> getThrownResults();

}
