package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Firefly handler for FF14.
 * @author Tamas_Szekeres
 */
@Component
public class FireflyHandler extends Ff14BeforeAfterRoundEnemyHandler {

    private static final int FIREFLY_EXTRA_DAMAGE = -2;
    private static final int FIREFLY_DISCHARGE_ATTACK = 4;

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final int index = command.getResolvedEnemies().indexOf(data.getCurrentEnemy());
        return results[index] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {

        final int[] roll = getGenerator().getRandomNumber(1);
        final String renderedRoll = getRenderer().render(getGenerator().getDefaultDiceSide(), roll);
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey("page.ff.label.random.after", renderedRoll, roll[0]);
        if (roll[0] < FIREFLY_DISCHARGE_ATTACK) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            character.changeStamina(FIREFLY_EXTRA_DAMAGE);
            final Enemy enemy = resolvationData.getEnemies().get(data.getPrimaryEnemy());
            messages.addKey("page.ff14.fight.fireflyDischargeDamage", enemy.getName());
        }

    }

    @Override
    public String[] getEnemyIds() {
        return new String[]{"25", "26", "27"};
    }
}
