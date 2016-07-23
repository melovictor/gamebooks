package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import org.springframework.stereotype.Component;

/**
 * Stone statue handler for FF34.
 * @author Tamas_Szekeres
 */
@Component
public class StoneStatueHandler extends Ff34BeforeAfterRoundEnemyHandler {

    private static final int ROLL_INFO_INSERT_POINT = 3;
    private static final int STONE_STATUE_DEFENSE_LIMIT = 3;
    private static final int HIGH_DAMAGE_ABSORPTION = 10;

    @Override
    public boolean shouldExecutePreHandler(final FfFightCommand command, final EnemyPrePostFightDataContainer data) {
        return true;
    }

    @Override
    public void executePreHandler(final FfFightCommand command, final EnemyPrePostFightDataContainer data) {
        final int[] randomNumber = getGenerator().getRandomNumber(1);
        data.setRoll(randomNumber);
        if (randomNumber[0] < STONE_STATUE_DEFENSE_LIMIT) {
            data.getCurrentEnemy().setDamageAbsorption(HIGH_DAMAGE_ABSORPTION);
            command.setLuckOnHit(false);
        } else {
            data.getCurrentEnemy().setDamageAbsorption(0);
        }
    }

    @Override
    public boolean shouldExecutePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.WIN;
    }

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final int[] roll = data.getRoll();
        record(command, roll, ROLL_INFO_INSERT_POINT);
        if (roll[0] < STONE_STATUE_DEFENSE_LIMIT) {
            command.getMessages().addKey("page.ff34.fight.stoneStatue.deflected");
        }
    }

    @Override
    protected String getEnemyId() {
        return "19";
    }

}
