package hu.zagor.gamebooks.ff.ff.bnc.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight.Ff11BeforeAfterRoundEnemyHandler;
import org.springframework.stereotype.Component;

/**
 * Handlers for the southron fighter pack.
 * @author Tamas_Szekeres
 */
@Component
public class SouthronHandler extends Ff11BeforeAfterRoundEnemyHandler {

    @Override
    public boolean shouldExecutePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        return character.getStamina() <= 2;
    }

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        for (final FfEnemy enemy : command.getResolvedEnemies()) {
            killEnemy(enemy);
        }
        resolvationData.getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "4001", 1);
    }

    @Override
    public String[] getEnemyIds() {
        return new String[]{"1", "2", "3", "4", "5", "6"};
    }

}
