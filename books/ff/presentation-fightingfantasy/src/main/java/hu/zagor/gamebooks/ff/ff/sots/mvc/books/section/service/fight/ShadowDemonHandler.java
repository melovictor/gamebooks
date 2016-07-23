package hu.zagor.gamebooks.ff.ff.sots.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import org.springframework.stereotype.Component;

/**
 * Handler for the Shadow Demon.
 * @author Tamas_Szekeres
 */
@Component
public class ShadowDemonHandler extends Ff20BeforeAfterRoundEnemyHandler {

    @Override
    public boolean shouldExecutePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.WIN;
    }

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final int[] randomNumber = getGenerator().getRandomNumber(2);
        final String dice = getRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber);
        command.getMessages().addKey("page.ff.label.random.after", dice, randomNumber[0]);
        if (randomNumber[0] % 2 == 1) {
            final FfEnemy currentEnemy = data.getCurrentEnemy();
            currentEnemy.setStamina(currentEnemy.getStamina() + 1);
        }
    }

    @Override
    protected String getEnemyId() {
        return "56";
    }
}
