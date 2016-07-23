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
 * Tyutchev handler for FF11.
 * @author Tamas_Szekeres
 */
@Component
public class TyutchevHandler extends Ff11BeforeAfterRoundEnemyHandler {

    private static final int TYUTCHEV_SURRENDER = 4;

    @Override
    public boolean shouldExecutePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.WIN;
    }

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
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
