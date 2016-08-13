package hu.zagor.gamebooks.lw.content.command.fight.roundresolver;

import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;

/**
 * Interrface to provide the result of a single clash in terms of who suffers what kind of damage.
 * @author Tamas_Szekeres
 */
public interface LwDamageResultProvider {

    /**
     * Returns the {@link LwDamageResult} object containing the damages to be suffered by LW and the enemy.
     * @param commandRatio the calculated combat ratio
     * @param randomRoll the result of the random roll
     * @param enemy the enemy against which we are currently fighting for
     * @param command the {@link LwFightCommand} object
     * @return the {@link LwDamageResult} bean containing damage information
     */
    LwDamageResult getLwDamageResult(int commandRatio, int randomRoll, LwEnemy enemy, LwFightCommand command);

}
