package hu.zagor.gamebooks.content.command.changeitem;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.util.ReflectionUtils;

/**
 * Main bean for resolving an item altering command.
 * @author Tamas_Szekeres
 */
public class ChangeItemCommandResolver extends TypeAwareCommandResolver<ChangeItemCommand> {

    @Override
    protected List<ParagraphData> doResolve(final ChangeItemCommand command, final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        character.getEquipment();
        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
        final FfItem item = (FfItem) itemHandler.getItem(character, command.getId());
        if (item != null) {
            final Field field = ReflectionUtils.findField(FfItem.class, command.getAttribute());
            ReflectionUtils.makeAccessible(field);
            final Integer newValue = command.getNewValue();
            if (newValue != null) {
                ReflectionUtils.setField(field, item, newValue);
            } else {
                int fieldValue = (int) ReflectionUtils.getField(field, item);
                fieldValue += command.getChangeValue();
                ReflectionUtils.setField(field, item, fieldValue);
            }
        }
        return null;
    }

}
