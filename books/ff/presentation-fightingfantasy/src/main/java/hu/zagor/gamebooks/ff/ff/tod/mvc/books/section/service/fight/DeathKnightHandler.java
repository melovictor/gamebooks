package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import org.springframework.stereotype.Component;

/**
 * Death Knight handler for FF11.
 * @author Tamas_Szekeres
 */
@Component
public class DeathKnightHandler extends Ff11BeforeAfterRoundEnemyHandler {
    private static final int DEATH_KNIGHT_STAMINA_LIMIT = 6;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE
            && resolvationData.getCharacterHandler().getAttributeHandler().resolveValue(resolvationData.getCharacter(), "stamina") <= DEATH_KNIGHT_STAMINA_LIMIT;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        killEnemy(resolvationData.getEnemies().get("10"));
        resolvationData.getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "4002", 1);
    }

    @Override
    protected String getEnemyId() {
        return "10";
    }
}
