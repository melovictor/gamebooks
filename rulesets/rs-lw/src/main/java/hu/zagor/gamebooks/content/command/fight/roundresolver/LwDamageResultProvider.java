package hu.zagor.gamebooks.content.command.fight.roundresolver;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Class to provide the result of a single clash in terms of who suffers what kind of damage.
 * @author Tamas_Szekeres
 */
@Component
public class LwDamageResultProvider {
    @Resource(name = "lwDamageResultTable") private Map<String, LwDamageResult> lwDamageResults;

    /**
     * Provides the {@link LwDamageResult} object for the given combat ratio and random number.
     * @param commandRatio the combat ratio value
     * @param randomNumber the thrown random number
     * @return the resolved {@link LwDamageResult}
     */
    public LwDamageResult getLwDamageResult(final int commandRatio, final int randomNumber) {
        final String key = commandRatio + "_" + randomNumber;
        LwDamageResult result = lwDamageResults.get(key);
        if (result == null) {
            if (commandRatio < 0) {
                result = getLwDamageResult(commandRatio + 1, randomNumber);
            } else {
                result = getLwDamageResult(commandRatio - 1, randomNumber);
            }
        }
        return result;
    }

}
