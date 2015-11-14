package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where multiple enemies must be fought, but always two of them at the same time.
 * @author Tamas_Szekeres
 */
@Component("alwaysTwoFightRoundResolver")
public class AlwaysTwoFightRoundResolver extends TwoByTwoFightRoundResolver {

    @Override
    void filterResolvedEnemies(final List<FfEnemy> resolvedEnemies, final List<String> enemies) {
        final FfEnemy enemyA = resolvedEnemies.get(0);
        final FfEnemy enemyB = resolvedEnemies.size() > 1 ? resolvedEnemies.get(1) : null;
        resolvedEnemies.clear();
        resolvedEnemies.add(enemyA);
        if (enemyB != null) {
            resolvedEnemies.add(enemyB);
        }
    }

}
