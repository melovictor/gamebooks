package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there are multiple enemies which must be fogught two-by-two, the next pair always waiting for both parties from the previous pair to die.
 * @author Tamas_Szekeres
 */
@Component("twoByTwoFightRoundResolver")
public class TwoByTwoFightRoundResolver extends AllAtOnceFightRoundResolver {

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        final List<FfEnemy> originalEnemies = new ArrayList<FfEnemy>(resolvedEnemies);

        final FightRoundResult[] resolvationResult = super.resolveRound(command, resolvationData, beforeRoundResult);

        int relevantEnemies = resolvedEnemies.size() - deadEnemies(resolvedEnemies);

        resolvedEnemies.clear();
        resolvedEnemies.addAll(originalEnemies);

        if (relevantEnemies == 0) {
            relevantEnemies = Math.min(2, originalEnemies.size() - 1);
        }
        command.setMaxEnemiesToDisplay(relevantEnemies);

        return resolvationResult;
    }

    private int deadEnemies(final List<FfEnemy> resolvedEnemies) {
        int totalDead = 0;
        for (final FfEnemy enemy : resolvedEnemies) {
            if (enemy.getStamina() <= 0) {
                totalDead++;
            }
        }
        return totalDead;
    }

    @Override
    List<FfEnemy> getRoundRelevantEnemies(final FfFightCommand command) {
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        final List<String> enemies = command.getEnemies();

        filterResolvedEnemies(resolvedEnemies, enemies);

        return resolvedEnemies;
    }

    void filterResolvedEnemies(final List<FfEnemy> resolvedEnemies, final List<String> enemies) {
        final FfEnemy enemyA = resolvedEnemies.get(0);
        final FfEnemy enemyB = resolvedEnemies.size() > 1 ? resolvedEnemies.get(1) : null;
        resolvedEnemies.clear();
        resolvedEnemies.add(enemyA);
        if (sameBlock(enemyA, enemies) && enemyB != null) {
            resolvedEnemies.add(enemyB);
        }
    }

    private boolean sameBlock(final FfEnemy enemyA, final List<String> enemies) {
        return enemies.indexOf(enemyA.getId()) % 2 == 0;
    }

}
