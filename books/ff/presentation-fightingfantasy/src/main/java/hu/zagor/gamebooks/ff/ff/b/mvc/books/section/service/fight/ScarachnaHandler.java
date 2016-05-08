package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.ff.b.character.Ff60Character;
import org.springframework.stereotype.Component;

/**
 * Handler for Scarachna for FF60.
 * @author Tamas_Szekeres
 */
@Component
public class ScarachnaHandler extends Ff60BeforeAfterRoundEnemyHandler {
    private static final int POISON_RESULT_POSITION = 4;
    private static final int RANDOM_ROLL_POSITION = 3;
    private static final int SCARACHNA_EXTRA_DAMAGE = -4;
    private static final int SCARACHNA_POISON_DAMAGE_RESTORABLE = 3;
    private static final int SCARACHNA_CRITICAL = 6;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final int[] randomNumber = getGenerator().getRandomNumber(1);
        final FightCommandMessageList messages = command.getMessages();
        final int rollResult = randomNumber[0];
        messages.addKey(RANDOM_ROLL_POSITION, "page.ff.label.random.after", getRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber), rollResult);

        if (rollResult == SCARACHNA_CRITICAL) {
            messages.addKey(POISON_RESULT_POSITION, "page.ff60.fight.scarachna.poison");
            final Ff60Character character = (Ff60Character) resolvationData.getCharacter();
            character.changeStamina(SCARACHNA_EXTRA_DAMAGE);
            character.setScarachnaPoison(character.getScarachnaPoison() + SCARACHNA_POISON_DAMAGE_RESTORABLE);
        }
    }

    @Override
    public String[] getEnemyIds() {
        return new String[]{"55", "87"};
    }
}
