package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.MapPosition;
import hu.zagor.gamebooks.support.locale.LocaleProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.stereotype.Component;

/**
 * Service for handling the tiger hunt.
 * @author Tamas_Szekeres
 */
@Component
public class HuntService {

    private static final String HUNT_ROUND = "huntRound";
    private static final String DOG_POSITION = "dogPosition";
    private static final String TIGER_POSITION = "tigerPosition";
    private static final int MAX_ROUNDS = 16;
    private static final int Y_MAX = 12;
    private static final int Y_MIN = 1;
    private static final int X_MAX = 8;
    private static final int X_MIN = 1;
    private static final String SECTION_TIGER_FOUND = "371";
    private static final String SECTION_TRAP = "262";
    private static final String SECTION_TIGER_FLED = "177";
    private static final int ROLL_1 = 1;
    private static final int ROLL_2 = 2;
    private static final int ROLL_3 = 3;
    private static final int ROLL_4 = 4;
    private static final int ROLL_5 = 5;
    private static final int ROLL_6 = 6;

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private LocaleProvider localeProvider;

    /**
     * Plays a single round of the hunt.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param info the {@link BookInformations} object
     * @return the result of the round
     */
    public HuntRoundResult playRound(final HttpSessionWrapper wrapper, final FfBookInformations info) {
        final HuntRoundResult result = new HuntRoundResult();
        final Character character = wrapper.getCharacter();
        final FfUserInteractionHandler interactionHandler = info.getCharacterHandler().getInteractionHandler();

        result.setTigerPosition(getTigerPosition(character, interactionHandler));
        result.setDogPosition(getDogPosition(character, interactionHandler));

        final int currentRound = getRound(character, interactionHandler);

        if (currentRound % 2 == 0) {
            tigerSteps(result);
        } else {
            dogSteps(result);
        }

        verifyPositions(result, currentRound);
        saveData(character, interactionHandler, currentRound, result);
        updateParagraphContent(wrapper.getParagraph(), result);

        if (result.isHuntFinished()) {
            wrapper.getParagraph().addValidMove(result.getNextSectionId());
        }

        return result;
    }

    private void updateParagraphContent(final Paragraph paragraph, final HuntRoundResult result) {
        final ParagraphData data = paragraph.getData();
        String text = data.getText();
        text = text.replaceAll("\\?colFirst", "");
        text = text.replace("</div><button id=\"ff23HuntTrigger\">", result.getRoundMessage() + "</div><button id=\"ff23HuntTrigger\">");
        text = text.replaceAll("tiger.png\" class=\"g[A-H] g[0-9]{1,2}\"", "tiger.png\" class=\"" + cssClassFromPosition(result.getTigerPosition()) + "\"");
        text = text.replaceAll("dog.png\" class=\"g[A-H] g[0-9]{1,2}\" \\/>", "dog.png\" class=\"" + cssClassFromPosition(result.getDogPosition()) + "\" />");
        if (result.isHuntFinished()) {
            text += "<input type='hidden' id='ff23HuntFinished' value='" + result.getNextSectionId() + "' />";
        }
        data.setText(text);
    }

    private String cssClassFromPosition(final String position) {
        String cssClass;
        final char posX = position.charAt(0);
        final int posY = Integer.parseInt(position.substring(1));
        if (posY < Y_MIN || posY > Y_MAX || posX == '@' || posX == 'I') {
            cssClass = "gX";
        } else {
            cssClass = "g" + posX + " g" + String.valueOf(posY);
        }
        return cssClass;
    }

    private void verifyPositions(final HuntRoundResult result, final int currentRound) {
        if ("C9".equals(result.getDogPosition())) {
            result.setHuntFinished(true);
            result.setNextSectionId(SECTION_TRAP);
        } else if (tigerCaughtUpWith(result)) {
            result.setHuntFinished(true);
            result.setNextSectionId(SECTION_TIGER_FOUND);
        } else if (outOfMap(result.getTigerPosition())) {
            result.setHuntFinished(true);
            result.setNextSectionId(SECTION_TIGER_FLED);
            result.setRoundMessage(result.getRoundMessage() + resolveTextKey("page.ff23.hunt.tiger.fled"));
        } else if (outOfMap(result.getDogPosition())) {
            result.setHuntFinished(true);
            result.setNextSectionId(SECTION_TIGER_FLED);
            result.setRoundMessage(result.getRoundMessage() + resolveTextKey("page.ff23.hunt.dog.lost"));
        } else if (currentRound >= MAX_ROUNDS) {
            result.setHuntFinished(true);
            result.setNextSectionId(SECTION_TIGER_FLED);
            result.setRoundMessage(result.getRoundMessage() + resolveTextKey("page.ff23.hunt.tiger.timeout"));
        }
    }

    private boolean outOfMap(final String position) {
        final MapPosition mapPosition = new MapPosition(position);
        return mapPosition.getX() < X_MIN || mapPosition.getX() > X_MAX || mapPosition.getY() < Y_MIN || mapPosition.getY() > Y_MAX;
    }

    private boolean tigerCaughtUpWith(final HuntRoundResult result) {
        final MapPosition dog = new MapPosition(result.getDogPosition());
        final MapPosition tiger = new MapPosition(result.getTigerPosition());

        return Math.abs(dog.getX() - tiger.getX()) < 2 && Math.abs(dog.getY() - tiger.getY()) < 2;
    }

    private void saveData(final Character character, final FfUserInteractionHandler interactionHandler, final int currentRound, final HuntRoundResult result) {
        interactionHandler.setInteractionState(character, HUNT_ROUND, String.valueOf(currentRound + 1));
        interactionHandler.setInteractionState(character, TIGER_POSITION, result.getTigerPosition());
        interactionHandler.setInteractionState(character, DOG_POSITION, result.getDogPosition());
    }

    private int getRound(final Character character, final FfUserInteractionHandler interactionHandler) {
        final String round = interactionHandler.getInteractionState(character, HUNT_ROUND);
        return round == null ? 1 : Integer.parseInt(round);
    }

    private void dogSteps(final HuntRoundResult result) {
        final MapPosition dogPosition = new MapPosition(result.getDogPosition());
        final MapPosition tigerPosition = new MapPosition(result.getTigerPosition());
        final int roll = getRandom();
        switch (roll) {
        case ROLL_1:
            move(dogPosition, tigerPosition);
            break;
        case ROLL_2:
            dogPosition.changeX(1);
            break;
        case ROLL_3:
            dogPosition.changeY(-1);
            dogPosition.changeY(-1);
            break;
        case ROLL_4:
            move(dogPosition, tigerPosition);
            move(dogPosition, tigerPosition);
            break;
        case ROLL_5:
            dogPosition.changeX(-1);
            break;
        default:
        }
        result.setDogPosition(dogPosition.getPosition());
        result.setRoundMessage(resolveTextKey("page.ff23.hunt.dog." + roll));
    }

    private void move(final MapPosition dogPosition, final MapPosition tigerPosition) {
        final int diffX = tigerPosition.getX() - dogPosition.getX();
        final int diffY = tigerPosition.getY() - dogPosition.getY();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            dogPosition.changeX((int) Math.signum(diffX));
        } else {
            dogPosition.changeY((int) Math.signum(diffY));
        }
    }

    private void tigerSteps(final HuntRoundResult result) {
        final MapPosition position = new MapPosition(result.getTigerPosition());
        final int roll = getRandom();
        switch (roll) {
        case ROLL_1:
            position.changeY(1);
            break;
        case ROLL_2:
            position.changeY(-1);
            break;
        case ROLL_3:
            position.changeX(-1);
            break;
        case ROLL_4:
            position.changeX(1);
            break;
        case ROLL_6:
            position.changeY(-1);
            break;
        default:
        }
        result.setTigerPosition(position.getPosition());
        result.setRoundMessage(resolveTextKey("page.ff23.hunt.tiger." + roll));
    }

    private String resolveTextKey(final String key) {
        return messageSource.getMessage(key, null, localeProvider.getLocale()) + "<br />";
    }

    private String getTigerPosition(final Character character, final FfUserInteractionHandler interactionHandler) {
        final String mapPosition = interactionHandler.getInteractionState(character, TIGER_POSITION);
        return mapPosition == null ? "E4" : mapPosition;
    }

    private String getDogPosition(final Character character, final FfUserInteractionHandler interactionHandler) {
        final String mapPosition = interactionHandler.getInteractionState(character, DOG_POSITION);
        return mapPosition == null ? "E12" : mapPosition;
    }

    private int getRandom() {
        return generator.getRandomNumber(1)[0];
    }

}
