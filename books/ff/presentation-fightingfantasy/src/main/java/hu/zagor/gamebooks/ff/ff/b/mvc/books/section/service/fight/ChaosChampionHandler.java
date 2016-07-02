package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Handler for the Chaos Champion in FF60.
 */
@Component
public class ChaosChampionHandler extends Ff60BeforeAfterRoundEnemyHandler {

    private static final int STRONGER_WEAKER_HIT_LIMIT = 4;
    private static final int RANDOM_ROLL_LOCATION = 3;
    private static final int ROUND_MESSAGE_LOCATION_WITH_ROLL = 4;

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        if (results[0] == FightRoundResult.WIN) {
            checkForLowerDamageCaused(command, data.getCurrentEnemy());
        } else if (results[0] == FightRoundResult.LOSE) {
            checkForHigherDamageSuffered(command, resolvationData);
        }
    }

    private void checkForHigherDamageSuffered(final FightCommand command, final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FightCommandMessageList messages = command.getMessages();
        final int[] roll = getReportRandomRoll(messages);
        if (roll[0] > STRONGER_WEAKER_HIT_LIMIT) {
            character.changeStamina(-1);
            messages.addKey(ROUND_MESSAGE_LOCATION_WITH_ROLL, "page.ff60.fight.chaoschampion.strongHit");
        }
    }

    private int[] getReportRandomRoll(final FightCommandMessageList messages) {
        final int[] roll = getGenerator().getRandomNumber(1);
        final String diceImage = getRenderer().render(getGenerator().getDefaultDiceSide(), roll);
        messages.addKey(RANDOM_ROLL_LOCATION, "page.ff.label.random.after", diceImage, roll[0]);
        return roll;
    }

    private void checkForLowerDamageCaused(final FightCommand command, final FfEnemy enemy) {
        final FightCommandMessageList messages = command.getMessages();
        final int[] roll = getReportRandomRoll(messages);
        if (roll[0] < STRONGER_WEAKER_HIT_LIMIT) {
            enemy.setStamina(enemy.getStamina() + 1);
            messages.addKey(ROUND_MESSAGE_LOCATION_WITH_ROLL, "page.ff60.fight.chaoschampion.weakHit");
        }
    }

    @Override
    protected String getEnemyId() {
        return "96";
    }
}
