package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import org.springframework.stereotype.Component;

/**
 * Handler for the two Thieves.
 * @author Tamas_Szekeres
 */
@Component
public class ThievesHandler extends Ff11BeforeAfterRoundEnemyHandler {

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final FfEnemy enemyA = (FfEnemy) resolvationData.getEnemies().get("27");
        final FfEnemy enemyB = (FfEnemy) resolvationData.getEnemies().get("28");
        return enemyA.getStamina() <= 0 || enemyB.getStamina() <= 0;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        killEnemy(resolvationData.getEnemies().get("27"));
        killEnemy(resolvationData.getEnemies().get("28"));
    }

    @Override
    public String[] getEnemyIds() {
        return new String[]{"27", "28"};
    }
}
