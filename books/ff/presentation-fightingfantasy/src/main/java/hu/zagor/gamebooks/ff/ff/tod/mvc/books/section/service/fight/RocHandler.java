package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import org.springframework.stereotype.Component;

/**
 * Roc handler for FF11.
 * @author Tamas_Szekeres
 */
@Component
public class RocHandler extends Ff11BeforeAfterRoundEnemyHandler {

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.WIN;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final int[] roll = getGenerator().getRandomNumber(1);
        final String renderedRoll = getRenderer().render(getGenerator().getDefaultDiceSide(), roll);
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey("page.ff.label.random.after", renderedRoll, roll[0]);
        if (roll[0] == 1) {
            killEnemy(resolvationData.getEnemies().get("46"));
            resolvationData.getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "4009", 1);
        }
    }

    @Override
    protected String getEnemyId() {
        return "46";
    }
}
