package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.MapPosition;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link PieceMover} interface that moves the tiger's piece.
 * @author Tamas_Szekeres
 */
@Component("tigerPieceMover")
public class TigerPieceMover extends AbstractPieceMover {

    @Override
    public void movePiece(final HuntRoundResult result) {
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

    @Override
    public String getPosition(final Character character, final FfUserInteractionHandler interactionHandler) {
        final String mapPosition = interactionHandler.getInteractionState(character, TIGER_POSITION);
        return mapPosition == null ? "E4" : mapPosition;
    }

}
