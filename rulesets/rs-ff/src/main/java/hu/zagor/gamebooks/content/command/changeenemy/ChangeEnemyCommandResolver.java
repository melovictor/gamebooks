package hu.zagor.gamebooks.content.command.changeenemy;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.mvel2.MVEL;
import org.springframework.util.ReflectionUtils;

/**
 * Main bean for resolving an enemy altering command.
 * @author Tamas_Szekeres
 */
public class ChangeEnemyCommandResolver extends TypeAwareCommandResolver<ChangeEnemyCommand> {

    @Override
    protected List<ParagraphData> doResolve(final ChangeEnemyCommand command, final ResolvationData resolvationData) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        String id = command.getId();

        if ("activeEnemy".equals(id)) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
            id = characterHandler.getInteractionHandler().peekLastFightCommand(character, "enemyId");
        }
        final FfEnemy enemy = (FfEnemy) enemies.get(id);
        final Field field = ReflectionUtils.findField(FfEnemy.class, command.getAttribute());
        ReflectionUtils.makeAccessible(field);
        final Integer newValue = command.getNewValue();
        if (newValue != null) {
            ReflectionUtils.setField(field, enemy, newValue);
        } else {
            int fieldValue = (int) ReflectionUtils.getField(field, enemy);
            final String changeValue = command.getChangeValue();
            try {
                fieldValue += Integer.parseInt(changeValue);
            } catch (final NumberFormatException ex) {
                final Number result = (Number) MVEL.eval(fieldValue + changeValue);
                fieldValue = result.intValue();
            }
            ReflectionUtils.setField(field, enemy, fieldValue);
        }
        return null;
    }

}
