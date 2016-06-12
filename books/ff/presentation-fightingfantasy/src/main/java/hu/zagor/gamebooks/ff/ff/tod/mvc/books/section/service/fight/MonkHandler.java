package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import org.springframework.stereotype.Component;

/**
 * Monk handler for FF11.
 * @author Tamas_Szekeres
 */
@Component
public class MonkHandler extends Ff11BeforeAfterRoundEnemyHandler {

    private static final int MONK_CRITICAL_HIT = 5;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final int[] roll = getGenerator().getRandomNumber(1);
        final String renderedRoll = getRenderer().render(getGenerator().getDefaultDiceSide(), roll);
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey("page.ff.label.random.after", renderedRoll, roll[0]);
        if (roll[0] >= MONK_CRITICAL_HIT) {
            killEnemy(resolvationData.getEnemies().get("37"));
            resolvationData.getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "4008", 1);
        }
    }

    @Override
    protected String getEnemyId() {
        return "37";
    }
}
