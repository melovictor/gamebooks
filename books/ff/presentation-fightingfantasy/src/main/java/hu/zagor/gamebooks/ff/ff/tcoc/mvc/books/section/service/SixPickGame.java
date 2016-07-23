package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.domain.SixPickBets;
import hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.domain.SixPickResult;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.stereotype.Component;

/**
 * Service class for playing the Six Pick game.
 * @author Tamas_Szekeres
 */
@Component
public class SixPickGame {

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator randomNumberGenerator;
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private LocaleProvider localeProvider;
    @Autowired
    private DiceResultRenderer diceResultRenderer;

    /**
     * Method to trigger the Six Pick game.
     * @param bets the bets the user made
     * @param character the {@link FfCharacter} object
     * @param paragraph the {@link Paragraph} object
     * @return the object containing the results of the round
     */
    public SixPickResult playRound(final FfCharacter character, final Paragraph paragraph, final SixPickBets bets) {
        final SixPickResult output = new SixPickResult();
        final int losings = bets.getN1() + bets.getN2() + bets.getN3() + bets.getN4() + bets.getN5() + bets.getN6();
        final int initialGold = character.getGold();

        if (losings > initialGold || losings == 0) {
            output.setStatus(1);
        } else {
            final ParagraphData data = paragraph.getData();
            data.setText(data.getText().replace("data-first-round=\"true\"", "data-first-round=\"false\""));
            final int[] thrownValues = randomNumberGenerator.getRandomNumber(1);
            final int thrownValue = thrownValues[0];
            final int winnings = 5 * getBaseBet(bets, thrownValue);
            final int balance = winnings - losings;
            final int newGold = initialGold + balance;
            character.setGold(newGold);

            output.setNewGold(newGold);
            final String diceRender = diceResultRenderer.render(randomNumberGenerator.getDefaultDiceSide(), thrownValues);
            output.setResponseText(messageSource.getMessage("page.ff2.sixPick.rollResult", new Object[]{diceRender, winnings, balance}, localeProvider.getLocale()));
            output.setStatus(0);
        }
        return output;
    }

    private int getBaseBet(final SixPickBets bets, final int thrownValue) {
        int baseBet = 0;
        try {
            baseBet = (int) bets.getClass().getMethod("getN" + thrownValue).invoke(bets);
        } catch (final Exception exception) {
        }
        return baseBet;
    }

}
