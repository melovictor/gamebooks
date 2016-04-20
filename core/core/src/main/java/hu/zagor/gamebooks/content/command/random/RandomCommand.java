package hu.zagor.gamebooks.content.command.random;

import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Main bean for storing data about a random roll.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class RandomCommand extends Command {

    private String label;
    private String diceConfig;
    private ParagraphData resultElse;
    private int diceResult;
    private transient int[] diceResults;
    private String diceResultText;
    private List<RandomResult> results = new ArrayList<>();
    private ParagraphData after;

    @Override
    public String getValidMove() {
        return "random";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setDiceConfig(final String diceConfig) {
        this.diceConfig = diceConfig;
    }

    /**
     * Add a new {@link RandomResult} object to the list of results.
     * @param randomResult the {@link RandomResult} object
     */
    public void addResult(final RandomResult randomResult) {
        results.add(randomResult);
    }

    public void setResultElse(final ParagraphData resultElse) {
        this.resultElse = resultElse;
    }

    @Override
    public CommandView getCommandView(final String rulesetPrefix) {
        final Map<String, Object> model = new HashMap<>();
        model.put("random", this);
        if (diceResults == null) {
            hideChoices(model);
        }

        return new CommandView(rulesetPrefix + "Random", model);
    }

    @Override
    public RandomCommand clone() throws CloneNotSupportedException {
        final RandomCommand cloned = (RandomCommand) super.clone();

        cloned.resultElse = cloneObject(resultElse);
        cloned.after = cloneObject(after);
        cloned.results = new ArrayList<>();
        for (final RandomResult result : results) {
            cloned.results.add(result.clone());
        }

        return cloned;
    }

    public String getDiceConfig() {
        return diceConfig;
    }

    public ParagraphData getResultElse() {
        return resultElse;
    }

    public List<RandomResult> getResults() {
        return results;
    }

    public int getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(final int diceResult) {
        this.diceResult = diceResult;
    }

    public String getDiceResultText() {
        return diceResultText;
    }

    public void setDiceResultText(final String diceResultText) {
        this.diceResultText = diceResultText;
    }

    public int[] getDiceResults() {
        return diceResults;
    }

    public void setDiceResults(final int[] diceResults) {
        this.diceResults = diceResults;
    }

    public ParagraphData getAfter() {
        return after;
    }

    public void setAfter(final ParagraphData after) {
        this.after = after;
    }

    public void setResults(final List<RandomResult> results) {
        this.results = results;
    }

}
