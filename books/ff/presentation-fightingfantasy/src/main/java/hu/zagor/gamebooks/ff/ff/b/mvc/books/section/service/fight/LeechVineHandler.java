package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Leech Vine handler for FF60.
 * @author Tamas_Szekeres
 */
@Component
public class LeechVineHandler extends Ff60BeforeAfterRoundEnemyHandler {

    @Override
    public boolean shouldExecutePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final Map<String, Integer> attackStrengths = command.getAttackStrengths();
        return attackStrengths.get("h_d1_38") == attackStrengths.get("h_d2_38");
    }

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        triggerFleeing(command, data.getCurrentEnemy());
    }

    @Override
    protected String getEnemyId() {
        return "38";
    }
}
