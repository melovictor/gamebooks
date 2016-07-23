package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Handler for the two Thieves.
 * @author Tamas_Szekeres
 */
@Component
public class ThievesHandler extends Ff11BeforeAfterRoundEnemyHandler {

    @Override
    public boolean shouldExecutePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        final FfEnemy enemyA = (FfEnemy) enemies.get("27");
        final FfEnemy enemyB = (FfEnemy) enemies.get("28");
        return enemyA.getStamina() <= 0 || enemyB.getStamina() <= 0;
    }

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        killEnemy(enemies.get("27"));
        killEnemy(enemies.get("28"));
    }

    @Override
    public String[] getEnemyIds() {
        return new String[]{"27", "28"};
    }
}
