package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import org.springframework.stereotype.Component;

/**
 * Chameleon handler for FF60.
 * @author Tamas_Szekeres
 */
@Component
public class ChameleonHandler extends Ff60BeforeAfterRoundEnemyHandler {
    private static final int CHAMELEON_CRITICAL_ATTACK_STRENGTH = 20;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE && highAttackStrength(command);
    }

    private boolean highAttackStrength(final FightCommand command) {
        return command.getAttackStrengths().get("54") >= CHAMELEON_CRITICAL_ATTACK_STRENGTH;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        triggerFleeing(command, data.getCurrentEnemy());
    }

    @Override
    protected String getEnemyId() {
        return "54";
    }
}
