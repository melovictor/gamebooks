package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import org.springframework.stereotype.Component;

/**
 * Handler for the Chiller for FF60.
 * @author Tamas_Szekeres
 */
@Component
public class ChillerHandler extends Ff60BeforeAfterRoundEnemyHandler {
    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return command.getRoundNumber() % 2 == 0;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        resolvationData.getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "4001", 1);
    }

    @Override
    protected String getEnemyId() {
        return "37";
    }
}
