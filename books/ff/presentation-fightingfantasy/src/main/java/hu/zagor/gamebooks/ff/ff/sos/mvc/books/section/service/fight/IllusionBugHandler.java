package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Handler for the illusion bug.
 * @author Tamas_Szekeres
 */
@Component
public class IllusionBugHandler extends Ff34BeforeAfterRoundEnemyHandler {

    private static final int ACID_DAMAGE_LIMIT = 4;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final int roll = rollRecord(1, command)[0];
        if (roll < ACID_DAMAGE_LIMIT) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            character.changeStamina(-1);
            command.getMessages().addKey("page.ff34.fight.illusionBug.acid");
        }
    }

    @Override
    protected String getEnemyId() {
        return "30";
    }
}
