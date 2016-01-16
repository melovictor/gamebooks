package hu.zagor.gamebooks.content.command.attributetest;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandView;
import java.util.HashMap;
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
    private FfParagraphData success;
    private FfParagraphData failure;
    private FfParagraphData failureEven;
    private FfParagraphData failureOdd;

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

    public void setSuccess(final FfParagraphData success) {
        this.success = success;
    }

    public void setFailure(final FfParagraphData failure) {
        this.failure = failure;
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

        cloned.success = cloneObject(success);
        cloned.failure = cloneObject(failure);
        cloned.failureEven = cloneObject(failureEven);
        cloned.failureOdd = cloneObject(failureOdd);

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

    public FfParagraphData getSuccess() {
        return success;
    }

    public FfParagraphData getFailure() {
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

}
