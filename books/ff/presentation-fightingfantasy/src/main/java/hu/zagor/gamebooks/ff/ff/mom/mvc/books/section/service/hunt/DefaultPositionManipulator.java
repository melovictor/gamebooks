package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.MapPosition;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the interface {@link PositionManipulator}.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultPositionManipulator implements PositionManipulator {

    private static final String HUNT_ROUND = "huntRound";
    private static final String DOG_POSITION = "dogPosition";
    private static final String TIGER_POSITION = "tigerPosition";

    private static final String SECTION_TIGER_FOUND = "2";
    private static final String SECTION_TRAP = "3";
    private static final String SECTION_TIGER_FLED = "1";

    private static final int MAX_ROUNDS = 16;
    private static final int Y_MAX = 12;
    private static final int Y_MIN = 1;
    private static final int X_MAX = 8;
    private static final int X_MIN = 1;

    @Autowired private HierarchicalMessageSource messageSource;
    @Autowired private LocaleProvider localeProvider;

    @Override
    public void verifyPositions(final HuntRoundResult result, final int currentRound) {
        if ("C9".equals(result.getDogPosition())) {
            result.setHuntFinished(true);
            result.setNextSectionPos(SECTION_TRAP);
        } else if (tigerCaughtUpWith(result)) {
            result.setHuntFinished(true);
            result.setNextSectionPos(SECTION_TIGER_FOUND);
        } else if (outOfMap(result.getTigerPosition())) {
            result.setHuntFinished(true);
            result.setNextSectionPos(SECTION_TIGER_FLED);
            result.setRoundMessage(result.getRoundMessage() + resolveTextKey("page.ff23.hunt.tiger.fled"));
        } else if (outOfMap(result.getDogPosition())) {
            result.setHuntFinished(true);
            result.setNextSectionPos(SECTION_TIGER_FLED);
            result.setRoundMessage(result.getRoundMessage() + resolveTextKey("page.ff23.hunt.dog.lost"));
        } else if (currentRound >= MAX_ROUNDS) {
            result.setHuntFinished(true);
            result.setNextSectionPos(SECTION_TIGER_FLED);
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

    private String resolveTextKey(final String key) {
        return messageSource.getMessage(key, null, localeProvider.getLocale()) + "<br />";
    }

    @Override
    public String cssClassFromPosition(final String position) {
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

    @Override
    public void saveData(final Character character, final FfUserInteractionHandler interactionHandler, final int currentRound, final HuntRoundResult result) {
        interactionHandler.setInteractionState(character, HUNT_ROUND, String.valueOf(currentRound + 1));
        interactionHandler.setInteractionState(character, TIGER_POSITION, result.getTigerPosition());
        interactionHandler.setInteractionState(character, DOG_POSITION, result.getDogPosition());
    }

    @Override
    public int getRound(final Character character, final FfUserInteractionHandler interactionHandler) {
        final String round = interactionHandler.getInteractionState(character, HUNT_ROUND);
        return round == null ? 1 : Integer.parseInt(round);
    }

}
