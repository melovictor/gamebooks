package hu.zagor.gamebooks.recording;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link ItemInteractionRecorder} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultItemInteractionRecorder extends AbstractPlaybackEventRecorder implements ItemInteractionRecorder {

    @Autowired
    private EnvironmentDetector environmentDetector;

    @Override
    public void recordItemTaking(final HttpSessionWrapper wrapper, final String itemId) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "takeItem(\"" + itemId + "\");\n");
        }
    }

    @Override
    public void recordItemConsumption(final HttpSessionWrapper wrapper, final String itemId) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "consumeItem(\"" + itemId + "\");\n");
        }
    }

    @Override
    public void changeItemEquipState(final HttpSessionWrapper wrapper, final String itemId) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "applyItem(\"" + itemId + "\");\n");
        }
    }

    @Override
    public void recordItemMarketMovement(final HttpSessionWrapper wrapper, final String transactionType, final String itemId) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "marketing(\"" + transactionType + "\", \"" + itemId + "\");\n");
        }
    }

    @Override
    public void recordItemReplacing(final HttpSessionWrapper wrapper, final String newItemId, final String oldItemId) {
        if (environmentDetector.isRecordState()) {
            add(wrapper, "replaceItem(\"" + newItemId + "\", \"" + oldItemId + "\");\n");
        }
    }
}
