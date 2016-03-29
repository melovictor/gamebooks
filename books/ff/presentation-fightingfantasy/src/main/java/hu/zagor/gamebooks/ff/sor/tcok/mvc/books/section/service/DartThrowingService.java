package hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.domain.DartThrowingResult;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Service handler for the Ten-Up game.
 * @author Tamas_Szekeres
 */
@Component
public class DartThrowingService {
    private static final int END_GAME_CHOICE_POS = 2;
    private static final int PLAY_AGAIN_CHOICE_POS = 1;
    private static final int EMPTY_CHOICE_POS = 3;
    private static final int TOTAL_PLAYERS = 4;
    private static final int PRIZE = 8;
    private static final int WIN_AMOUNT = 10;
    @Autowired @Qualifier("d10") private RandomNumberGenerator generator;
    @Resource(name = "sor4TenUpResults") private Map<String, Integer> throwValues;

    /**
     * Handler for the dart throwing contest.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param info the {@link FfBookInformations} object from the current book
     * @return the result
     */
    public DartThrowingResult throwDart(final HttpSessionWrapper wrapper, final FfBookInformations info) {
        final DartThrowingResult result = new DartThrowingResult();
        final int x = generator.getRandomNumber(1)[0] - 1;
        final int y = generator.getRandomNumber(1)[0] - 1;

        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfUserInteractionHandler interactionHandler = info.getCharacterHandler().getInteractionHandler();
        int total = getTotal(character, interactionHandler);
        final int round = getRound(character, interactionHandler);

        final Integer integer = throwValues.get(x + "_" + y);
        if (integer != null) {
            total += integer;
        }

        interactionHandler.setInteractionState(character, "tenUpTotal", String.valueOf(total));

        result.setX(x);
        result.setY(y);
        result.setNewTotal(total);
        final boolean winner = total == WIN_AMOUNT || total == -WIN_AMOUNT;
        if (winner) {
            result.setWinner(winner);
            if (round % TOTAL_PLAYERS == 0) {
                info.getCharacterHandler().getItemHandler().addItem(character, "gold", PRIZE);
                result.setWinnerName("hero");
            } else {
                result.setWinnerName("guard");
            }
            result.setGold(character.getGold());
        }

        updateTexts(wrapper, result, round);

        return result;
    }

    private void updateTexts(final HttpSessionWrapper wrapper, final DartThrowingResult result, final int round) {
        final ParagraphData data = wrapper.getParagraph().getData();
        String text = data.getText();

        text = text.replaceAll("<span id=\"currentScore\">-{0,1}[0-9]+<\\/span>", "<span id=\"currentScore\">" + result.getNewTotal() + "</span>");
        text = text.replaceAll("<input type=\"hidden\" id=\"nextThrowerId\" value=\"[0-3]\" \\/>",
            "<input type=\"hidden\" id=\"nextThrowerId\" value=\"" + ((round + 1) % TOTAL_PLAYERS) + "\" \\/>");
        if (result.isWinner()) {
            text = text.replaceAll("<button id=\"sor4TenUpTrigger\">[^<]+</button>", "");
            if (result.getGold() < 2) {
                data.getChoices().removeByPosition(PLAY_AGAIN_CHOICE_POS);
                data.getChoices().removeByPosition(END_GAME_CHOICE_POS);
            } else {
                data.getChoices().removeByPosition(EMPTY_CHOICE_POS);
            }
        }

        data.setText(text);
    }

    private int getRound(final Character character, final FfUserInteractionHandler interactionHandler) {
        final String totalString = interactionHandler.getInteractionState(character, "tenUpRound");
        int round;
        if (totalString == null) {
            round = 0;
        } else {
            round = Integer.parseInt(totalString);
        }
        interactionHandler.setInteractionState(character, "tenUpRound", String.valueOf(round + 1));
        return round;
    }

    private int getTotal(final Character character, final FfUserInteractionHandler interactionHandler) {
        final String totalString = interactionHandler.getInteractionState(character, "tenUpTotal");
        int total;
        if (totalString == null) {
            total = 0;
        } else {
            total = Integer.parseInt(totalString);
        }
        return total;
    }

}
