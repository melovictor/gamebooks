package hu.zagor.gamebooks.ff.ff.sots.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import org.springframework.stereotype.Component;

/**
 * Handler for the Phoenix.
 * @author Tamas_Szekeres
 */
@Component
public class PhoenixHandler extends Ff20BeforeAfterRoundEnemyHandler {

    private static final int DAMAGE_ROLL_POSITION = 3;

    @Override
    public boolean shouldExecutePreHandler(final FightCommand command, final BasicEnemyPrePostFightDataContainer data) {
        return true;
    }

    @Override
    public void executePreHandler(final FightCommand command, final BasicEnemyPrePostFightDataContainer data) {
        final int[] randomNumber = getGenerator().getRandomNumber(1);
        data.getCurrentEnemy().setStaminaDamage(randomNumber[0] - 1);
    }

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final String nito = resolvationData.getCharacterHandler().getInteractionHandler().peekInteractionState(resolvationData.getCharacter(), "nito");
        return nito == null && results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final int dmg = data.getCurrentEnemy().getStaminaDamage();
        final int[] randomNumber = new int[]{dmg + 1, dmg + 1};
        final String dice = getRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber);
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey(DAMAGE_ROLL_POSITION, "page.ff.label.random.after", dice, dmg + 1);
    }

    @Override
    protected String getEnemyId() {
        return "45";
    }
}
