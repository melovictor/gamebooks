package hu.zagor.gamebooks.lw.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckStubCommandResolver;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import java.lang.reflect.InvocationTargetException;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * Item checker that detects whether the player has a specific kai/magnakai/grand master discipline or not. It doesn't work with weaponskills, since it is not a normal kai skill,
 * and is not meant to be used together with that one!
 * @author Tamas_Szekeres
 */
public class ItemCheckKaiDisciplineCommandResolver implements ItemCheckStubCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        ParagraphData data = null;
        final String discipline = parent.getId();
        final String checkType = parent.getCheckType();
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        try {
            final Object disciplines = ReflectionUtils.findMethod(LwCharacter.class, "get" + StringUtils.capitalize(checkType) + "Disciplines").invoke(character);
            final boolean hasSkill = (boolean) ReflectionUtils.findMethod(disciplines.getClass(), "is" + StringUtils.capitalize(discipline)).invoke(disciplines);
            if (hasSkill) {
                data = parent.getHave();
            } else {
                data = parent.getDontHave();
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException("Failed to fetch " + checkType + " discipline " + discipline + " state because it doesn't exists.", e);
        }
        return data;
    }

}
