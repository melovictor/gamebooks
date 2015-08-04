package hu.zagor.gamebooks.ff.wm.tds.content.command.attributetest;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;

/**
 * An attribute test command that doesn't have any success or failure paths but still can slip through the initial assertion checks.
 * @author Tamas_Szekeres
 */
public class LyingAttributeTestCommand extends AttributeTestCommand {

    private FfParagraphData data;

    @Override
    public FfParagraphData getSuccess() {
        final FfParagraphData toReturn = data;
        data = null;
        return toReturn;
    }

    /**
     * Resets the fake data entry to be used for the assert validation.
     */
    public void reset() {
        data = new FfParagraphData();
    }
}
