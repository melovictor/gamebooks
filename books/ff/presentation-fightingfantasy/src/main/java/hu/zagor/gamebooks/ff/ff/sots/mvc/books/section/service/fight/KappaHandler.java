package hu.zagor.gamebooks.ff.ff.sots.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Handler for the Kappas.
 * @author Tamas_Szekeres
 */
@Component
public class KappaHandler extends Ff20BeforeAfterRoundEnemyHandler {

    private static final int KAPPA_GIVE_UP_STAMINA = 4;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        return data.getCurrentEnemy().getStamina() <= KAPPA_GIVE_UP_STAMINA;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        killEnemy(enemies.get("31"));
        killEnemy(enemies.get("32"));
        killEnemy(enemies.get("33"));
    }

    @Override
    public String[] getEnemyIds() {
        return new String[]{"31", "32", "33"};
    }
}
