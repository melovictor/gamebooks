package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.EnemyDependentFfBookPreFightHandlingService;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfBookPreFightHandlingService;
import hu.zagor.gamebooks.support.messages.MessageSource;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of the {@link FfBookPreFightHandlingService} interface for the FF34 book.
 * @author Tamas_Szekeres
 */
public class Ff34BookPreFightHandlingService extends EnemyDependentFfBookPreFightHandlingService {

    private static final int FIREBALL_DAMAGE = 6;
    private static final String FIREBALL = "4103";
    private static final String SPEED = "4107";

    @Autowired private MessageSource messageSource;

    @Override
    public FfItem handlePreFightItemUsage(final FfBookInformations info, final HttpSessionWrapper wrapper, final String itemId) {
        if (FIREBALL.equals(itemId)) {
            final FfEnemy enemy = getEnemy(wrapper, info);
            enemy.setStamina(enemy.getStamina() - FIREBALL_DAMAGE);
            appendText(wrapper.getParagraph().getData(), "page.ff34.prefight.fireball", enemy.getCommonName());
        } else if (SPEED.equals(itemId)) {
            reduceEnemySkill(wrapper);
            appendText(wrapper.getParagraph().getData(), "page.ff34.prefight.speed");
        }
        info.getCharacterHandler().getItemHandler().removeItem(wrapper.getCharacter(), itemId, 1);
        return null;
    }

    private void reduceEnemySkill(final HttpSessionWrapper wrapper) {
        final FightCommand command = (FightCommand) wrapper.getParagraph().getItemsToProcess().get(0).getCommand();
        final Map<String, Enemy> enemies = wrapper.getEnemies();
        for (final String enemyId : command.getEnemies()) {
            final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
            enemy.setSkill(enemy.getSkill() - 2);
        }
    }

    private String resolveKey(final String textKey, final Object... params) {
        return messageSource.getMessage(textKey, params);
    }

    private void appendText(final ParagraphData data, final String textKey, final Object... params) {
        String text = data.getText();

        text += "<p>" + resolveKey(textKey, params) + "</p>";

        data.setText(text);
    }

}
