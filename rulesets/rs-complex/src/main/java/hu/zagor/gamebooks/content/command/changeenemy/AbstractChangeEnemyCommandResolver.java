package hu.zagor.gamebooks.content.command.changeenemy;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.mvel2.MVEL;
import org.springframework.util.ReflectionUtils;

/**
 * Main bean for resolving an enemy altering command.
 * @author Tamas_Szekeres
 * @param <E> the exact type for the {@link Enemy}
 */
public abstract class AbstractChangeEnemyCommandResolver<E extends Enemy> extends TypeAwareCommandResolver<ChangeEnemyCommand> {

    @Override
    protected List<ParagraphData> doResolve(final ChangeEnemyCommand command, final ResolvationData resolvationData) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        final String id = getEnemyId(command, resolvationData);
        final Object enemy = enemies.get(id);
        final Field field = ReflectionUtils.findField(enemy.getClass(), command.getAttribute());
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

    /**
     * Returns the id of the enemy that needs to be altered.
     * @param command the {@link ChangeEnemyCommand}
     * @param resolvationData the {@link ResolvationData}
     * @return the id of the enemy
     */
    protected abstract String getEnemyId(final ChangeEnemyCommand command, final ResolvationData resolvationData);
}
