package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there is one or two enemies, and one supporter. While there are only three combatants, the single enemy always attacks back to the one
 * with the highest Attack Strength. When there are two enemies, the original one and the ally fight with each other, the newcomer fights agianst us.
 * @author Tamas_Szekeres
 */
@Component("highestMainSupportedFightRoundResolver")
public class HighestMainSupportedSor2FightRoundResolver implements FightRoundResolver {
    @Autowired @Qualifier("highestMainSupportedSingleSor2FightRoundResolver") private FightRoundResolver oneEnemyResolver;
    @Autowired @Qualifier("highestMainSupportedDualSor2FightRoundResolver") private FightRoundResolver twoEnemiesResolver;
    @Autowired @Qualifier("allAtOnceFightRoundResolver") private FightRoundResolver allAtOnceResolver;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        FightRoundResult[] result;
        if (!(resolvationData.getCharacter() instanceof FfAllyCharacter)) {
            if (command.getResolvedAllies().size() == 0) {
                result = allAtOnceResolver.resolveRound(command, resolvationData, beforeRoundResult);
            } else {
                final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
                if (resolvedEnemies.size() == 1) {
                    result = oneEnemyResolver.resolveRound(command, resolvationData, beforeRoundResult);
                } else {
                    result = twoEnemiesResolver.resolveRound(command, resolvationData, beforeRoundResult);
                }
            }
        } else {
            result = new FightRoundResult[]{FightRoundResult.WIN};
        }
        return result;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        throw new UnsupportedOperationException();
    }

}
