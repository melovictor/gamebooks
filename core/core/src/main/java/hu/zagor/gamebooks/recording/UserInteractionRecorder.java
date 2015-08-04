package hu.zagor.gamebooks.recording;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;

/**
 * Interface for recording events about different user interactions.
 * @author Tamas_Szekeres
 */
public interface UserInteractionRecorder {

    /**
     * Records the fact that an attribute test has been executed.
     * @param wrapper the {@link HttpSessionWrapper} object
     */
    void recordAttributeTest(HttpSessionWrapper wrapper);

    /**
     * Records the fact that a random roll has been executed.
     * @param wrapper the {@link HttpSessionWrapper} object
     */
    void recordRandomRoll(HttpSessionWrapper wrapper);

    /**
     * Prepares the UI for battling.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param luckOnHit marker for luck test on hitting the enemy
     * @param luckOnDefense marker for luck test on being hit by the enemy
     * @param luckOnOther marker for luck test on other eventualities
     */
    void prepareFightCommand(HttpSessionWrapper wrapper, Boolean luckOnHit, Boolean luckOnDefense, Boolean luckOnOther);

    /**
     * Records the fact that a round of battle has been initiated.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param enemyId the id of the enemy to fight
     */
    void recordFightCommand(HttpSessionWrapper wrapper, String enemyId);

    /**
     * Records the fact that the user has provided a response.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param responseText the response the user has provided
     * @param elapsedTime the time under which the user has provided his response
     */
    void recordUserResponse(HttpSessionWrapper wrapper, String responseText, int elapsedTime);

    /**
     * Records the fact that the market screen has been closed.
     * @param wrapper the {@link HttpSessionWrapper} object
     */
    void recordMarketClosing(HttpSessionWrapper wrapper);

}
