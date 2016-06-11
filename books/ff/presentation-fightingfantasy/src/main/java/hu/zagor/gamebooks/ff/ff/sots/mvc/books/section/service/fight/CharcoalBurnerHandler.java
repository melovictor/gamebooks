package hu.zagor.gamebooks.ff.ff.sots.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import org.springframework.stereotype.Component;

/**
 * Handler for second charcoal burner handler.
 * @author Tamas_Szekeres
 */
@Component
public class CharcoalBurnerHandler extends Ff20BeforeAfterRoundEnemyHandler {

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final FfEnemy enemy = (FfEnemy) resolvationData.getEnemies().get("22");
        return enemy.getStamina() <= 0;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final FfEnemy currentEnemy = data.getCurrentEnemy();
        currentEnemy.setFleeAtStamina(currentEnemy.getStamina());
    }

    @Override
    protected String getEnemyId() {
        return "23";
    }

}
