package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * FF60-specific round resolver.
 * @author Tamas_Szekeres
 */
@Component("allAtOnceff60FightRoundResolver")
public class AllAtOnceFf60FightRoundResolver implements FightRoundResolver {
    @Autowired @Qualifier("allAtOnceFightRoundResolver") private FightRoundResolver decorated;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final int roundNumber = command.getRoundNumber();

        final List<String> enemyIds = command.getEnemies();
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        FfEnemy removedEnemy = null;
        if (roundNumber % 2 == 1 && enemyIds.contains("80") && resolvedEnemies.size() > 1) {
            removedEnemy = resolvedEnemies.remove(resolvedEnemies.size() - 1);
        }

        final FightRoundResult[] resolveRound = decorated.resolveRound(command, resolvationData, beforeRoundResult);

        if (removedEnemy != null) {
            resolvedEnemies.add(removedEnemy);
        }

        return resolveRound;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        decorated.resolveFlee(command, resolvationData);
    }

}
