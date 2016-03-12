package hu.zagor.gamebooks.content.command.attributetest;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Main bean for storing data about a skill test.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class AttributeTestCommand extends Command {

    private int result;
    private String resultString;

    private String against;
    private String compactAgainst;
    private int againstNumeric;
    private int add;
    private String configurationName;
    private AttributeTestSuccessType successType;

    private String label;
    private boolean compact;
    private List<SuccessFailureDataContainer> success = new ArrayList<>();
    private List<SuccessFailureDataContainer> failure = new ArrayList<>();
    private FfParagraphData failureEven;
    private FfParagraphData failureOdd;
    private FfParagraphData skipped;
    private FfParagraphData after;

    private boolean testSuccess;
    private boolean canSkip;
    private String skipText;

    public void setAgainst(final String against) {
        this.against = against;
    }

    public void setAdd(final int add) {
        this.add = add;
    }

    public AttributeTestSuccessType getSuccessType() {
        return successType;
    }

    public void setSuccessType(final AttributeTestSuccessType successType) {
        this.successType = successType;
    }

    public void setLabel(final String label) {
        this.label = fixText(label);
    }

    /**
     * Adds a new {@link SuccessFailureDataContainer} to the success pack.
     * @param success the object to add
     */
    public void addSuccess(final SuccessFailureDataContainer success) {
        this.success.add(success);
    }

    /**
     * Adds a new {@link SuccessFailureDataContainer} to the failure pack.
     * @param failure the object to add
     */
    public void addFailure(final SuccessFailureDataContainer failure) {
        this.failure.add(failure);
    }

    @Override
    public CommandView getCommandView(final String rulesetPrefix) {
        final Map<String, Object> model = new HashMap<>();
        model.put("attribTest", this);
        if (resultString == null) {
            hideChoices(model);
        }

        return new CommandView(rulesetPrefix + "AttributeTest", model);
    }

    @Override
    public String getValidMove() {
        return "attributeTest";
    }

    public void setConfigurationName(final String configurationName) {
        this.configurationName = configurationName;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public AttributeTestCommand clone() throws CloneNotSupportedException {
        final AttributeTestCommand cloned = (AttributeTestCommand) super.clone();

        cloned.success = cloneList(success);
        cloned.failure = cloneList(failure);
        cloned.after = cloneObject(after);
        cloned.skipped = cloneObject(skipped);
        cloned.failureEven = cloneObject(failureEven);
        cloned.failureOdd = cloneObject(failureOdd);

        return cloned;
    }

    private List<SuccessFailureDataContainer> cloneList(final List<SuccessFailureDataContainer> orig) throws CloneNotSupportedException {
        final List<SuccessFailureDataContainer> cloned = new ArrayList<>();
        for (final SuccessFailureDataContainer container : orig) {
            cloned.add(cloneObject(container));
        }
        return cloned;
    }

    public String getAgainst() {
        return against;
    }

    public int getAdd() {
        return add;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public List<SuccessFailureDataContainer> getSuccess() {
        return success;
    }

    public List<SuccessFailureDataContainer> getFailure() {
        return failure;
    }

    public int getResult() {
        return result;
    }

    public void setResult(final int result) {
        this.result = result;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(final String resultString) {
        this.resultString = resultString;
    }

    public FfParagraphData getFailureEven() {
        return failureEven;
    }

    public void setFailureEven(final FfParagraphData failureEven) {
        this.failureEven = failureEven;
    }

    public FfParagraphData getFailureOdd() {
        return failureOdd;
    }

    public void setFailureOdd(final FfParagraphData failureOdd) {
        this.failureOdd = failureOdd;
    }

    public int getAgainstNumeric() {
        return againstNumeric;
    }

    public void setAgainstNumeric(final int againstNumeric) {
        this.againstNumeric = againstNumeric;
    }

    public boolean isCompact() {
        return compact;
    }

    public void setCompact(final boolean compact) {
        this.compact = compact;
    }

    public String getCompactAgainst() {
        return compactAgainst;
    }

    public void setCompactAgainst(final String compactAgainst) {
        this.compactAgainst = compactAgainst;
    }

    public boolean isTestSuccess() {
        return testSuccess;
    }

    public void setTestSuccess(final boolean testSuccess) {
        this.testSuccess = testSuccess;
    }

    public boolean isCanSkip() {
        return canSkip;
    }

    public void setCanSkip(final boolean canSkip) {
        this.canSkip = canSkip;
    }

    public String getSkipText() {
        return skipText;
    }

    public void setSkipText(final String skipText) {
        this.skipText = skipText;
    }

    public FfParagraphData getSkipped() {
        return skipped;
    }

    public void setSkipped(final FfParagraphData skipped) {
        this.skipped = skipped;
    }

    public FfParagraphData getAfter() {
        return after;
    }

    public void setAfter(final FfParagraphData after) {
        this.after = after;
    }

}
