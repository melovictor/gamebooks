package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.MapPosition;

import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link PieceMover} interface that moves the dog's piece.
 * @author Tamas_Szekeres
 */
@Component("dogPieceMover")
public class DogPieceMover extends AbstractPieceMover {

    @Override
    public void movePiece(final HuntRoundResult result) {
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

    @Override
    public String getPosition(final Character character, final FfUserInteractionHandler interactionHandler) {
        final String mapPosition = interactionHandler.getInteractionState(character, DOG_POSITION);
        return mapPosition == null ? "E12" : mapPosition;
    }
}
