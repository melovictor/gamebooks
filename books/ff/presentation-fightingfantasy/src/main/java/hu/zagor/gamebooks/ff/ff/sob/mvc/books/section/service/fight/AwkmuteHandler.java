package hu.zagor.gamebooks.ff.ff.sob.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import org.springframework.stereotype.Component;

/**
 * Fight handler for Awkmute.
 * @author Tamas_Szekeres
 */
@Component
public class AwkmuteHandler extends Ff16BeforeAfterRoundEnemyHandler {

    private static final int ROLL_RESULT_INSERT_POSITION = 3;
    private static final int LUCK_DAMAGE_ABOVE = 4;
    private static final int STAMINA_DAMAGE_BELOW = 3;

    @Override
    public boolean shouldExecutePreHandler(final FfFightCommand command, final EnemyPrePostFightDataContainer data) {
        return true;
    }

    @Override
    public void executePreHandler(final FfFightCommand command, final EnemyPrePostFightDataContainer data) {
        final int[] randomNumber = getGenerator().getRandomNumber(1);
        data.setRandomRoll(randomNumber);
        final FfEnemy enemy = command.getResolvedEnemies().get(0);
        enemy.setStaminaDamage(0);
        enemy.setSkillDamage(0);
        enemy.setLuckDamage(0);
        if (randomNumber[0] < STAMINA_DAMAGE_BELOW) {
            enemy.setStaminaDamage(2);
        } else {
            command.setLuckOnDefense(false);
            if (randomNumber[0] > LUCK_DAMAGE_ABOVE) {
                enemy.setLuckDamage(1);
            } else {
                enemy.setSkillDamage(1);
            }
        }
    }

    @Override
    public boolean shouldExecutePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey(ROLL_RESULT_INSERT_POSITION, "page.ff.label.random.after", getRenderer().render(getGenerator().getDefaultDiceSide(), data.getRandomRoll()),
            data.getRandomRoll()[0]);
    }

    @Override
    protected String getEnemyId() {
        return "25";
    }

}
