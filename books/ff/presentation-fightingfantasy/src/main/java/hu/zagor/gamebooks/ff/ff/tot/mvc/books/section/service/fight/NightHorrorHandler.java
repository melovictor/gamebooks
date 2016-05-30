package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import org.springframework.stereotype.Component;

/**
 * Handler for the Night Horror in FF14.
 * @author Tamas_Szekeres
 */
@Component
public class NightHorrorHandler extends Ff14BeforeAfterRoundEnemyHandler {

    private static final int NIGHT_HORROR_ATTACK_BLOCK = 4;

    @Override
    public boolean shouldExecutePreHandler(final FightCommand command, final EnemyPrePostFightDataContainer data) {
        return true;
    }

    @Override
    public void executePreHandler(final FightCommand command, final EnemyPrePostFightDataContainer data) {
        data.setEnemyStamina(data.getCurrentEnemy().getStamina());
    }

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.WIN;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final int[] roll = getGenerator().getRandomNumber(1);
        final String renderedRoll = getRenderer().render(getGenerator().getDefaultDiceSide(), roll);
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey("page.ff.label.random.after", renderedRoll, roll[0]);
        if (roll[0] < NIGHT_HORROR_ATTACK_BLOCK) {
            data.getCurrentEnemy().setStamina(data.getEnemyStamina());
            messages.addKey("page.ff14.fight.nightHorror.blocked");
        }
    }

    @Override
    protected String getEnemyId() {
        return "35";
    }
}
