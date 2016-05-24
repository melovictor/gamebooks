package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Tyutchev handler for FF11.
 * @author Tamas_Szekeres
 */
@Component
public class TyutchevHandler extends Ff11BeforeAfterRoundEnemyHandler {

    private static final int TYUTCHEV_SURRENDER = 4;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.WIN;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        final FfEnemy enemy = (FfEnemy) enemies.get("18");
        if (enemy.getStamina() <= TYUTCHEV_SURRENDER) {
            killEnemy(enemy);
            killEnemy(enemies.get("19"));
        }
    }

    @Override
    protected String getEnemyId() {
        return "18";
    }
}
