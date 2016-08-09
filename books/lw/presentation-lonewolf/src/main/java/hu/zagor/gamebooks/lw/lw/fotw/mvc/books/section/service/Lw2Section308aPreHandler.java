package hu.zagor.gamebooks.lw.lw.fotw.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.Money;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.messages.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section handler for LW2.
 * @author Tamas_Szekeres
 */
@Component
public class Lw2Section308aPreHandler implements CustomPrePostSectionHandler {
    private static final int BET_AMOUNT = 3;
    @Autowired private MessageSource messageSource;
    @Autowired @Qualifier("d10") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final DiceConfiguration d100 = new DiceConfiguration(2, 0, 9, true);
        final int[] player1 = generator.getRandomNumber(d100);
        final int[] player2 = generator.getRandomNumber(d100);
        final int[] hero = generator.getRandomNumber(d100);

        final ParagraphData data = wrapper.getParagraph().getData();
        final StringBuilder builder = new StringBuilder();
        reportRoll(builder, player1, "player1");
        reportRoll(builder, player2, "player2");
        reportRoll(builder, hero, "hero");

        final LwCharacter character = (LwCharacter) wrapper.getCharacter();
        final Money money = character.getMoney();

        if (hero[0] == 0) {
            reportResult(builder, "portholes");
            money.setGoldCrowns(money.getGoldCrowns() + 2 * BET_AMOUNT);
        } else {
            if (hero[0] > player1[0] && hero[0] > player2[0]) {
                reportResult(builder, "hero");
                money.setGoldCrowns(money.getGoldCrowns() + 2 * BET_AMOUNT);
            } else {
                reportResult(builder, "player");
                money.setGoldCrowns(money.getGoldCrowns() - BET_AMOUNT);
            }
        }

        data.setText(data.getText().replace("{0}", builder.toString()));
    }

    private void reportResult(final StringBuilder builder, final String resultPostfix) {
        final String result = messageSource.getMessage("page.lw2.label.portholes.highest." + resultPostfix);
        builder.append(result);
    }

    private void reportRoll(final StringBuilder builder, final int[] rolls, final String keyPostfix) {
        rolls[0] = rolls[1] * generator.getDefaultDiceSide() + rolls[2];
        final String rollResult = messageSource.getMessage("page.lw2.label.portholes.rollResult." + keyPostfix, renderer.render(generator.getDefaultDiceSide(), rolls),
            rolls[0]);
        builder.append(rollResult + "<br />");
    }

}
