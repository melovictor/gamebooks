package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.lw.content.command.fight.roundresolver.LwPrePostEnemyFightHandler;
import org.springframework.stereotype.Component;

/**
 * Fight handler for the knights.
 * @author Tamas_Szekeres
 */
@Component
public class Lw2PrePost15Handler implements LwPrePostEnemyFightHandler {

    @Override
    public boolean shouldExecutePre(final LwFightCommand command, final ResolvationData resolvationData) {
        return false;
    }

    @Override
    public Object executePre(final LwFightCommand command, final ResolvationData resolvationData) {
        return null;
    }

    @Override
    public boolean shouldExecutePost(final LwFightCommand command, final ResolvationData resolvationData) {
        return command.getRoundNumber() == 1;
    }

    @Override
    public void executePost(final LwFightCommand command, final ResolvationData resolvationData, final Object preGenData) {
        resolvationData.getCharacterHandler().getItemHandler().removeItem(resolvationData.getCharacter(), "50001", 2);
    }

}
