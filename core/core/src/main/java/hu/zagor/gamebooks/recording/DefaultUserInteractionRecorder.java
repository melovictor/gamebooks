package hu.zagor.gamebooks.recording;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link UserInteractionRecorder} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultUserInteractionRecorder extends AbstractPlaybackEventRecorder implements UserInteractionRecorder {

    @Autowired
    private EnvironmentDetector environmentDetector;

    @Override
    public void recordAttributeTest(final HttpSessionWrapper wrapper) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "doAttributeCheck();\n");
        }
    }

    @Override
    public void recordRandomRoll(final HttpSessionWrapper wrapper) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "doRandomRoll();\n");
        }
    }

    @Override
    public void prepareFightCommand(final HttpSessionWrapper wrapper, final Boolean luckOnHit, final Boolean luckOnDefense, final Boolean luckOnOther) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "prepareLuckSettings(" + luckOnHit + ", " + luckOnDefense + ", " + luckOnOther + ");\n");
        }
    }

    @Override
    public void recordFightCommand(final HttpSessionWrapper wrapper, final String enemyId) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "attackEnemy(\"" + enemyId + "\");\n");
        }
    }

    @Override
    public void recordUserResponse(final HttpSessionWrapper wrapper, final String responseText, final int elapsedTime) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "giveResponse(\"" + responseText + "\", " + elapsedTime + ");\n");
        }
    }

    @Override
    public void recordMarketClosing(final HttpSessionWrapper wrapper) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "closeMarket();\n");
        }
    }
}
