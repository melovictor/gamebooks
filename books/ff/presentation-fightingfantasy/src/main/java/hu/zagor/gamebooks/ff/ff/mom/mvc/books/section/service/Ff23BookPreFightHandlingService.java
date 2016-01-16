package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service;

import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfBookPreFightHandlingService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
 * Implementation of the {@link FfBookPreFightHandlingService} interface for the FF23 book.
 * @author Tamas_Szekeres
 */
public class Ff23BookPreFightHandlingService implements FfBookPreFightHandlingService {

    private static final String HEVER_HORN = "3016";
    @Resource(name = "hornResistantEnemies") private List<String> hornResistantEnemies;

    @Override
    public FfItem handlePreFightItemUsage(final FfBookInformations info, final HttpSessionWrapper wrapper, final String itemId) {
        FfItem item = null;
        if (HEVER_HORN.equals(itemId)) {
            reduceEnemySkill(wrapper);
            item = (FfItem) info.getCharacterHandler().getItemHandler().getItem(wrapper.getCharacter(), itemId);
        }
        return item;
    }

    private void reduceEnemySkill(final HttpSessionWrapper wrapper) {
        final FightCommand command = (FightCommand) wrapper.getParagraph().getData().getCommands().get(0);
        final Map<String, Enemy> enemies = wrapper.getEnemies();
        for (final String enemyId : command.getEnemies()) {
            final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
            if (!hornResistantEnemies.contains(enemyId)) {
                enemy.setSkill(enemy.getSkill() - 1);
            }
        }
    }

}
