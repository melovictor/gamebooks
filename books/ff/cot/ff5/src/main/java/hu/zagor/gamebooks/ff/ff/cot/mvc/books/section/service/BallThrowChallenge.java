package hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.stereotype.Component;

/**
 * Class to play the ball throwing game.
 * @author Tamas_Szekeres
 */
@Component
public class BallThrowChallenge {

    private static final int PRIZE = 5;
    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer renderer;

    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private LocaleProvider localeProvider;

    /**
     * Do play the game.
     * @param character the {@link FfCharacter} object
     * @param data the {@link ParagraphData} object
     */
    public void playGame(final FfCharacter character, final ParagraphData data) {
        final String rootText = data.getText();
        final StringBuilder builder = new StringBuilder();

        int round = 0;
        int[] thrown;
        final Locale locale = localeProvider.getLocale();
        do {
            round++;
            thrown = generator.getRandomNumber(1);
            final String dice = renderer.render(6, thrown);
            final String throwEvent = messageSource.getMessage("page.ff5.ballthrow." + (round % 2 == 0 ? "opponent" : "hero") + "Throw", new Object[]{dice}, locale);

            builder.append(throwEvent + "<br />");
        } while (thrown[0] > 1);

        if (round % 2 == 0) {
            character.setGold(character.getGold() + PRIZE);
        } else {
            character.setGold(character.getGold() - PRIZE);
        }

        final String throwEvent = messageSource.getMessage("page.ff5.ballthrow." + (round % 2 == 0 ? "hero" : "opponent") + "Win", null, locale);
        builder.append(throwEvent);

        data.setText(rootText.replace("||placeholder||", builder.toString()));
    }

}
