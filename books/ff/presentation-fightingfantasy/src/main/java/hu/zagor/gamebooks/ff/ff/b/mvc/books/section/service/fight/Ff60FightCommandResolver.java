package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightCommandResolver;
import java.util.List;
import java.util.Map;

/**
 * Book-specific fight command resolver for FF60.
 * @author Tamas_Szekeres
 */
public class Ff60FightCommandResolver extends FightCommandResolver {
    @Override
    protected List<ParagraphData> doResolve(final FightCommand command, final ResolvationData resolvationData) {
        final CharacterItemHandler itemHandler = resolvationData.getInfo().getCharacterHandler().getItemHandler();
        final boolean hasChainmail = itemHandler.hasEquippedItem(resolvationData.getCharacter(), "3018");
        prepareEnemies(command, resolvationData.getEnemies(), hasChainmail ? -1 : 0);

        return super.doResolve(command, resolvationData);
    }

    private void prepareEnemies(final FightCommand command, final Map<String, Enemy> enemies, final int attackStrengthBonus) {
        for (final String enemyId : command.getEnemies()) {
            final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
            enemy.setAttackStrengthBonus(attackStrengthBonus);
        }
    }
}
