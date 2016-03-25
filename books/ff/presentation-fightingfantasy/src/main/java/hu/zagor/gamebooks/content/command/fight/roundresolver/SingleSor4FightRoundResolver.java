package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link FightRoundResult} interface for resolving single fight rounds in SOR4.
 * @author Tamas_Szekeres
 */
@Component("singlesor4FightRoundResolver")
public class SingleSor4FightRoundResolver extends SingleSorFightRoundResolver {
    @Override
    void doWinFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        super.doWinFight(command, result, enemyIdx, dto);

        if ("1".equals(dto.getEnemy().getId())) {
            command.setFleeAllowed(true);
            final FightFleeData fleeData = new FightFleeData();
            fleeData.setSufferDamage(true);
            command.setFleeData(fleeData);
        }
    }

    @Override
    void doLoseFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        super.doLoseFight(command, result, enemyIdx, dto);

        if ("1".equals(dto.getEnemy().getId())) {
            command.setFleeAllowed(false);
        }
    }

    @Override
    void doTieFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        super.doTieFight(command, result, enemyIdx, dto);

        if ("1".equals(dto.getEnemy().getId())) {
            command.setFleeAllowed(false);
        }
    }
}
