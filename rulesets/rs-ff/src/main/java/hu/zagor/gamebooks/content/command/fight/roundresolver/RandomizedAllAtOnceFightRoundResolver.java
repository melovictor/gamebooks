package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.RandomEnemyPickerLine;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there are multiple fighting parties and in every round the hero is targeting one of them at random.
 * @author Tamas_Szekeres
 */
@Component("randomizedAllAtOnceFightRoundResolver")
public class RandomizedAllAtOnceFightRoundResolver extends AllAtOnceFightRoundResolver {

    @Autowired @Qualifier("d6RandomGenerator") private RandomNumberGenerator generator;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final List<String> enemies = command.getEnemies();
        if (enemies.size() > 1) {
            randomizeEnemy(command, resolvationData);
        }
        return super.resolveRound(command, resolvationData, beforeRoundResult);
    }

    private void randomizeEnemy(final FightCommand command, final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final String lastFightCommand = interactionHandler.peekLastFightCommand(character, "enemyId");
        if (lastFightCommand != null) {
            final int[] randomNumber = generator.getRandomNumber(1);
            final String enemyId = pickEnemy(command.getEnemies(), randomNumber[0]);
            interactionHandler.setFightCommand(character, "enemyId", enemyId);
            recordLine(command, new RandomEnemyPickerLine(randomNumber[0], resolvationData.getEnemies().get(enemyId).getName()));
        }
    }

    private String pickEnemy(final List<String> enemies, final int diceResult) {
        final int total = enemies.size();
        final int pos = (diceResult - 1) * total / generator.getDefaultDiceSide();
        return enemies.get(pos);
    }

}
