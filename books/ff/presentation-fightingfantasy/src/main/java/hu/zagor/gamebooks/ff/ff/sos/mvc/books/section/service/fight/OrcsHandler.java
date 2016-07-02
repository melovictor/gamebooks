package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Orc handler for FF34.
 * @author Tamas_Szekeres
 */
@Component
public class OrcsHandler extends Ff34BeforeAfterRoundEnemyHandler {

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return data.getCurrentEnemy().getStamina() <= 0;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        final FfEnemy secondOrc = (FfEnemy) enemies.get("23");
        killEnemy(secondOrc);
    }

    @Override
    protected String getEnemyId() {
        return "22";
    }

}
