package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Handler for Scarface for FF11.
 * @author Tamas_Szekeres
 */
@Component
public class ScarfaceHandler extends Ff11BeforeAfterRoundEnemyHandler {
    private static final int SCARFACE_FLEEIGN = 5;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results.length == 2 && results[0] == FightRoundResult.WIN;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        final FfEnemy scarface = (FfEnemy) enemies.get("22");
        if (scarface.getStamina() <= SCARFACE_FLEEIGN) {
            killEnemy(enemies.get("23"));
        }
    }

    @Override
    protected String getEnemyId() {
        return "22";
    }
}
