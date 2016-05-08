package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Handler for the Hook Man in FF60.
 * @author Tamas_Szekeres
 */
@Component
public class HookManHandler extends Ff60BeforeAfterRoundEnemyHandler {
    private static final int HOOK_MAN_EXTRA_HIT = 4;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final FightCommandMessageList messages = command.getMessages();
        messages.switchToPostRoundMessages();
        final int[] randomNumber = getGenerator().getRandomNumber(1);
        final String dice = getRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber);
        messages.addKey("page.ff.label.random.after", dice, randomNumber[0]);
        if (randomNumber[0] > HOOK_MAN_EXTRA_HIT) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            character.changeStamina(-1);
        }
    }

    @Override
    protected String getEnemyId() {
        return "19";
    }

}
