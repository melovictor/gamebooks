package hu.zagor.gamebooks.ff.ff.sots.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.ff.ff.sots.character.Ff20Character;
import org.springframework.stereotype.Component;

/**
 * Handler against the Dai-Oni.
 * @author Tamas_Szekeres
 */
@Component
public class DaiOniHandler extends Ff20BeforeAfterRoundEnemyHandler {

    private static final int LUCK_UPPER_LIMIT = 4;
    private static final int LUCK_LOWER_LIMIT = 3;
    private static final int STAMINA_EXTRA_DEDUCTION = -2;
    private static final int SKILL_EXTRA_DEDUCTION = -1;
    private static final int LUCK_EXTRA_DEDUCTION = -1;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final int[] randomNumber = getGenerator().getRandomNumber(1);
        final String dice = getRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber);
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey("page.ff.label.random.after", dice, randomNumber[0]);
        final Ff20Character character = (Ff20Character) resolvationData.getCharacter();
        if (randomNumber[0] < LUCK_LOWER_LIMIT) {
            character.changeStamina(STAMINA_EXTRA_DEDUCTION);
            messages.addKey("page.ff20.fight.daioni.stamina");
        } else if (randomNumber[0] > LUCK_UPPER_LIMIT) {
            character.changeSkill(SKILL_EXTRA_DEDUCTION);
            messages.addKey("page.ff20.fight.daioni.skill");
        } else {
            character.changeLuck(LUCK_EXTRA_DEDUCTION);
            messages.addKey("page.ff20.fight.daioni.luck");
        }
    }

    @Override
    protected String getEnemyId() {
        return "54";
    }

}
