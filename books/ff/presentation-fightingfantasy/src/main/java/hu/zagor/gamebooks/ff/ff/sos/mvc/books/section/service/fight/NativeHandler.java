package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Natives handler for FF34.
 * @author Tamas_Szekeres
 */
@Component
public class NativeHandler extends Ff34BeforeAfterRoundEnemyHandler {

    private static final int POISON_ACTIVATION = 3;

    @Override
    public boolean shouldExecutePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final int idx = Integer.parseInt(data.getCurrentEnemy().getId()) - 5;
        return results[idx] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final int rolledTotal = rollRecord(1, command)[0];
        if (rolledTotal > POISON_ACTIVATION) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            character.changeStamina(-1);
            command.getMessages().addKey("page.ff34.fight.native.extraDamage");
        }
    }

    @Override
    public String[] getEnemyIds() {
        return new String[]{"5", "6"};
    }
}
