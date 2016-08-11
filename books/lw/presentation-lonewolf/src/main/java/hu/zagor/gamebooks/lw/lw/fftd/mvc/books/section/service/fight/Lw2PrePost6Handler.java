package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.lw.content.command.fight.roundresolver.LwPrePostEnemyFightHandler;
import org.springframework.stereotype.Component;

/**
 * Handler for the wrestling match.
 * @author Tamas_Szekeres
 */
@Component
public class Lw2PrePost6Handler implements LwPrePostEnemyFightHandler {

    @Override
    public boolean shouldExecutePre(final LwFightCommand command, final ResolvationData resolvationData) {
        return resolvationData.getCharacterHandler().getInteractionHandler().peekInteractionState(resolvationData.getCharacter(), "startingEndurance") == null;
    }

    @Override
    public Object executePre(final LwFightCommand command, final ResolvationData resolvationData) {
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        resolvationData.getCharacterHandler().getInteractionHandler().setInteractionState(character, "startingEndurance", String.valueOf(character.getEndurance()));
        return null;
    }

    @Override
    public boolean shouldExecutePost(final LwFightCommand command, final ResolvationData resolvationData) {
        return !command.isOngoing();
    }

    @Override
    public void executePost(final LwFightCommand command, final ResolvationData resolvationData, final Object preGenData) {
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        final int startingEndurance = Integer
            .parseInt(resolvationData.getCharacterHandler().getInteractionHandler().peekInteractionState(character, "startingEndurance"));
        character.setEndurance(startingEndurance);
    }

}
