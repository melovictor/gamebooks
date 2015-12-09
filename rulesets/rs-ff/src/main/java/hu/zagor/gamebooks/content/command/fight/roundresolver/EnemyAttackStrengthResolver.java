package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;

/**
 * Interface for calculating an enemy's attack strength.
 * @author Tamas_Szekeres
 */
public interface EnemyAttackStrengthResolver {
    /**
     * Rolls the attack strength for the enemies, taking into account if a specific enemy always needs to have a given attack strength.
     * @param enemy the enemy for which to roll attack strength
     * @param command the {@link FightCommand} object
     * @return the rolled values in the usual format
     */
    int[] getEnemyAttackStrength(final FfEnemy enemy, final FightCommand command);

}
