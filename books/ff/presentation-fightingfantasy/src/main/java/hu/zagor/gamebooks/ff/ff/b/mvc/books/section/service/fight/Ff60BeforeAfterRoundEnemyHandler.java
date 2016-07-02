package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicBeforeAfterRoundEnemyHandler;
import java.util.List;

/**
 * Marker class to be used by all FF60 handlers.
 * @author Tamas_Szekeres
 */
public abstract class Ff60BeforeAfterRoundEnemyHandler extends BasicBeforeAfterRoundEnemyHandler<EnemyPrePostFightDataContainer> {
    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return true;
    }

    void triggerFleeing(final FightCommand command, final FfEnemy enemy) {
        final List<FightOutcome> win = command.getWin();
        win.clear();
        final FightOutcome outcome = new FightOutcome();
        outcome.setParagraphData(command.getFlee());
        win.add(outcome);
        enemy.setStamina(0);
    }

}
