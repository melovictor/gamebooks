package hu.zagor.gamebooks.renderer;

import hu.zagor.gamebooks.content.dice.DiceConfiguration;

import org.springframework.stereotype.Component;

/**
 * Renders the div elements for a given dice result.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultDiceResultRenderer implements DiceResultRenderer {

    @Override
    public String render(final DiceConfiguration diceConfiguration, final int[] results) {
        return render(diceConfiguration.getMaxValue() - diceConfiguration.getMinValue() + 1, results);
    }

    @Override
    public String render(final int diceSide, final int[] results) {

        final StringBuilder builder = new StringBuilder();
        for (int i = 1; i < results.length; i++) {
            builder.append("<span class='diced" + diceSide + results[i] + "'></span>");
        }

        return builder.toString();
    }

}
