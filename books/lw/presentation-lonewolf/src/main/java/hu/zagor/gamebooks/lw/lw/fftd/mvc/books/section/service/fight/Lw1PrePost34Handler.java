package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.LwEnemy;
import hu.zagor.gamebooks.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.content.command.fight.roundresolver.LwPrePostEnemyFightHandler;
import org.springframework.stereotype.Component;

/**
 * Handler for the robber in LW1.
 * @author Tamas_Szekeres
 */
@Component
public class Lw1PrePost34Handler implements LwPrePostEnemyFightHandler {

    private static final int INTERRUPT_BATTLE_ROUND = 4;

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
        return command.getRoundNumber() == INTERRUPT_BATTLE_ROUND;
    }

    @Override
    public void executePost(final LwFightCommand command, final ResolvationData resolvationData, final Object preGenData) {
        final LwEnemy enemy = command.getResolvedEnemies().get(0);
        if (enemy.getEndurance() > 0) {
            resolvationData.getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "50004", 1);
            enemy.setEndurance(0);
        }
    }

}
