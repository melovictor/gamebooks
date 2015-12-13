package hu.zagor.gamebooks.content.command.fight.subresolver.autolose;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.EnemyStatusEvaluator;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link AutoLoseHandler} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultAutoLoseHandler implements AutoLoseHandler {

    @Autowired private EnemyStatusEvaluator enemyStatusEvaluator;

    @Override
    public void checkAutoEvents(final FightCommand command, final List<ParagraphData> resolveList, final ResolvationData resolvationData) {
        final int autoLoseAfterRound = command.getAutoLoseRound();
        final int autoLoseStamina = command.getAutoLoseStamina();
        if ((loseBasedOnRound(command, autoLoseAfterRound) && !enemyStatusEvaluator.enemiesAreDead(command.getResolvedEnemies(), command.getRoundNumber()))
            || loseBasedOnStamina(command, resolvationData, autoLoseStamina)) {
            command.setOngoing(false);
            resolveList.add(command.getLose());
        }
    }

    private boolean loseBasedOnStamina(final FightCommand command, final ResolvationData resolvationData, final int autoLoseStamina) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final FfCharacter character = getFightingCharacter((FfCharacter) resolvationData.getCharacter(), command.getResolvedAllies());
        return attributeHandler.resolveValue(character, "stamina") <= autoLoseStamina;
    }

    private FfCharacter getFightingCharacter(final FfCharacter character, final List<FfAllyCharacter> resolvedAllies) {
        FfCharacter fightingCharacter = null;
        if (character instanceof FfAllyCharacter) {
            for (final FfAllyCharacter ally : resolvedAllies) {
                if (fightingCharacter == null || fightingCharacter.getStamina() <= 0) {
                    fightingCharacter = ally;
                }
            }
        } else {
            fightingCharacter = character;
        }
        return fightingCharacter;
    }

    private boolean loseBasedOnRound(final FightCommand command, final int autoLoseAfterRound) {
        return autoLoseAfterRound > 0 && command.getRoundNumber() == autoLoseAfterRound;
    }

}
