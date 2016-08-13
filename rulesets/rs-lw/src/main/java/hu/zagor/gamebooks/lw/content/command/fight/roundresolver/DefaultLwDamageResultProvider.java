package hu.zagor.gamebooks.lw.content.command.fight.roundresolver;

import hu.zagor.gamebooks.content.CloneFailedException;
import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;
import java.util.Map;
import javax.annotation.Resource;

/**
 * Default implementation of the {@link LwDamageResultProvider} interface.
 * @author Tamas_Szekeres
 */
public class DefaultLwDamageResultProvider implements LwDamageResultProvider {
    @Resource(name = "lwDamageResultTable") private Map<String, LwDamageResult> lwDamageResults;

    @Override
    public LwDamageResult getLwDamageResult(final int commandRatio, final int randomNumber, final LwEnemy enemy, final LwFightCommand command) {
        final String key = commandRatio + "_" + randomNumber;
        LwDamageResult result;
        try {
            result = lwDamageResults.get(key).clone();
        } catch (final CloneNotSupportedException e) {
            throw new CloneFailedException(e);
        }
        if (result == null) {
            if (commandRatio < 0) {
                result = getLwDamageResult(commandRatio + 1, randomNumber, enemy, command);
            } else {
                result = getLwDamageResult(commandRatio - 1, randomNumber, enemy, command);
            }
        }
        return result;
    }

}
