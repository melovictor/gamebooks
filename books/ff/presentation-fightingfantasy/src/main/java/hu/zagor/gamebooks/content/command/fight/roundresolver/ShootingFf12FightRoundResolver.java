package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import org.springframework.stereotype.Component;

/**
 * Class for playing a single round of blaster battle in book FF12.
 * @author Tamas_Szekeres
 */
@Component("shootingff12FightRoundResolver")
public class ShootingFf12FightRoundResolver implements FightRoundResolver {

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FightRoundResult[] results = new FightRoundResult[command.getResolvedEnemies().size()];

        return results;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        throw new UnsupportedOperationException("Fleeing from battle is not supported in this fight mode.");
    }

}
