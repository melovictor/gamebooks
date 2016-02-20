package hu.zagor.gamebooks.ff.ff.sa.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.sa.character.Ff12Character;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfBookPreFightHandlingService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implementation of the {@link FfBookPreFightHandlingService} interface for the FF12 book.
 * @author Tamas_Szekeres
 */
public class Ff12BookPreFightHandlingService implements FfBookPreFightHandlingService {
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;

    @Override
    public FfItem handlePreFightItemUsage(final FfBookInformations info, final HttpSessionWrapper wrapper, final String itemId) {
        final Ff12Character character = (Ff12Character) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        itemHandler.removeItem(character, itemId, 1);

        final List<FfEnemy> enemies = getActiveEnemies(wrapper);

        final Paragraph paragraph = wrapper.getParagraph();
        if ("108".equals(paragraph.getId())) {
            for (final FfEnemy enemy : enemies) {
                heal(enemy);
            }
        }
        for (final FfEnemy enemy : enemies) {
            damageEnemy(wrapper, enemy);
        }

        return null;
    }

    private void damageEnemy(final HttpSessionWrapper wrapper, final FfEnemy enemy) {
        final int[] randomNumber = generator.getRandomNumber(1);
        final FightCommand command = (FightCommand) wrapper.getParagraph().getItemsToProcess().get(0).getCommand();
        command.getMessages().addKey("page.ff12.fight.pre.grenade", randomNumber[0], enemy.getCommonName());
        enemy.setStamina(enemy.getStamina() - randomNumber[0]);
    }

    private List<FfEnemy> getActiveEnemies(final HttpSessionWrapper wrapper) {
        final List<FfEnemy> enemies = new ArrayList<>();

        final Map<String, Enemy> enemyList = wrapper.getEnemies();
        final FightCommand command = (FightCommand) wrapper.getParagraph().getItemsToProcess().get(0).getCommand();
        for (final String enemyId : command.getEnemies()) {
            enemies.add((FfEnemy) enemyList.get(enemyId));
        }

        return enemies;
    }

    private void heal(final FfEnemy enemy) {
        enemy.setStamina(enemy.getStamina() + 1);
    }

}
