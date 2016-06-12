package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Captain handler for ff11.
 * @author Tamas_Szekeres
 */
@Component
public class CaptainHandler extends Ff11BeforeAfterRoundEnemyHandler {
    private static final int CAPTAIN_CRITICAL_HIT_LIMIT = 18;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        return command.getEnemies().contains("7") && command.getAttackStrengths().containsKey("7") && command.getAttackStrengths().get("7") >= CAPTAIN_CRITICAL_HIT_LIMIT
            && results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        killEnemy(enemies.get("7"));
        killEnemy(enemies.get("8"));
        resolvationData.getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "4001", 1);
    }

    @Override
    protected String getEnemyId() {
        return "7";
    }
}
