package hu.zagor.gamebooks.lw.lw.fotw.content.command.fight.roundresolver;

import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.lw.content.command.fight.roundresolver.LwDamageResult;
import hu.zagor.gamebooks.lw.content.command.fight.roundresolver.LwDamageResultProvider;

/**
 * Special damage result provider for LW2.
 * @author Tamas_Szekeres
 */
public class Lw2DamageResultProvider implements LwDamageResultProvider {
    private LwDamageResultProvider decorated;

    @Override
    public LwDamageResult getLwDamageResult(final int commandRatio, final int randomRoll, final LwEnemy enemy, final LwFightCommand command) {
        final LwDamageResult result = decorated.getLwDamageResult(commandRatio, randomRoll, enemy, command);
        if ("17".equals(enemy.getId()) && command.getRoundNumber() <= 2) {
            result.setLwKilled(false);
            result.setLwSuffers(0);
        }

        return result;
    }

    public void setDecorated(final LwDamageResultProvider decorated) {
        this.decorated = decorated;
    }

}
