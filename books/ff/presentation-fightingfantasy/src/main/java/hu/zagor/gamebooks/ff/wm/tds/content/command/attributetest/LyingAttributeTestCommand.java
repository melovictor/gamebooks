package hu.zagor.gamebooks.ff.wm.tds.content.command.attributetest;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.attributetest.SuccessFailureDataContainer;
import java.util.ArrayList;
import java.util.List;

/**
 * An attribute test command that doesn't have any success or failure paths but still can slip through the initial assertion checks.
 * @author Tamas_Szekeres
 */
public class LyingAttributeTestCommand extends AttributeTestCommand {

    private FfParagraphData data;

    @Override
    public List<SuccessFailureDataContainer> getSuccess() {
        final List<SuccessFailureDataContainer> list = new ArrayList<>();
        list.add(new SuccessFailureDataContainer(data, null));
        data = null;
        return list;
    }

    /**
     * Resets the fake data entry to be used for the assert validation.
     */
    public void reset() {
        data = new FfParagraphData();
    }
}
