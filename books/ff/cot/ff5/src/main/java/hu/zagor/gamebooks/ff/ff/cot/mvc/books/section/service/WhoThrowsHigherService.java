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
 * Service for handling the game Who throws higher, played against three Dwarved.
 * @author Tamas_Szekeres
 */
@Component
public class WhoThrowsHigherService {

    private static final int DICE_SIDE = 6;
    private static final int LOSING = 2;
    private static final int WINNING = 6;
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private LocaleProvider localeProvider;

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer renderer;

    /**
     * Plays a single round of the game.
     * @param character the {@link FfCharacter} object
     * @param root the root {@link ParagraphData} object
     */
    public void playGame(final FfCharacter character, final ParagraphData root) {
        final StringBuilder builder = new StringBuilder();

        final int[] dwarfA = generator.getRandomNumber(2);
        final int[] dwarfB = generator.getRandomNumber(2);
        final int[] dwarfC = generator.getRandomNumber(2);
        final int[] hero = generator.getRandomNumber(2);
        final int dwarfMax = Math.max(dwarfA[0], Math.max(dwarfB[0], dwarfC[0]));
        String messageKey;
        if (dwarfMax > hero[0]) {
            character.setGold(character.getGold() - LOSING);
            messageKey = "page.ff5.whoThrowsHigher.lose";
        } else if (dwarfMax < hero[0]) {
            character.setGold(character.getGold() + WINNING);
            messageKey = "page.ff5.whoThrowsHigher.win";
        } else {
            messageKey = "page.ff5.whoThrowsHigher.tie";
        }

        final Locale locale = localeProvider.getLocale();

        builder.append(messageSource.getMessage("page.ff5.whoThrowsHigher.dwarfA", new Object[]{renderer.render(DICE_SIDE, dwarfA), dwarfA[0]}, locale) + "<br />");
        builder.append(messageSource.getMessage("page.ff5.whoThrowsHigher.dwarfB", new Object[]{renderer.render(DICE_SIDE, dwarfB), dwarfB[0]}, locale) + "<br />");
        builder.append(messageSource.getMessage("page.ff5.whoThrowsHigher.dwarfC", new Object[]{renderer.render(DICE_SIDE, dwarfC), dwarfC[0]}, locale) + "<br />");
        builder.append(messageSource.getMessage("page.ff5.whoThrowsHigher.hero", new Object[]{renderer.render(DICE_SIDE, hero), hero[0]}, locale) + "<br />");

        builder.append(messageSource.getMessage(messageKey, null, locale));

        String rootText = root.getText();
        rootText += "[p]" + builder.toString() + "[/p]";
        root.setText(rootText);
    }
}
