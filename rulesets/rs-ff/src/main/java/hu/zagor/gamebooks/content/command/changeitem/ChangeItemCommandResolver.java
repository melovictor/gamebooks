package hu.zagor.gamebooks.content.command.changeitem;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.mvel2.MVEL;
import org.springframework.util.ReflectionUtils;

/**
 * Main bean for resolving an item altering command.
 * @author Tamas_Szekeres
 */
public class ChangeItemCommandResolver extends TypeAwareCommandResolver<ChangeItemCommand> {

    @Override
    protected List<ParagraphData> doResolve(final ChangeItemCommand command, final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
        final List<Item> items = getItemsToChange(command, character, itemHandler);
        for (final Item item : items) {
            final Field field = ReflectionUtils.findField(FfItem.class, command.getAttribute());
            ReflectionUtils.makeAccessible(field);
            final Integer newValue = command.getNewValue();
            if (newValue != null) {
                ReflectionUtils.setField(field, item, newValue);
            } else {
                int fieldValue = (int) ReflectionUtils.getField(field, item);
                final String changeValue = command.getChangeValue();
                try {
                    fieldValue += Integer.parseInt(changeValue);
                } catch (final NumberFormatException ex) {
                    final Number result = (Number) MVEL.eval(fieldValue + changeValue);
                    fieldValue = result.intValue();
                }
                ReflectionUtils.setField(field, item, fieldValue);
            }
        }
        return null;
    }

    private List<Item> getItemsToChange(final ChangeItemCommand command, final FfCharacter character, final FfCharacterItemHandler itemHandler) {
        List<Item> itemsToChange;
        if ("equippedWeapon".equals(command.getId())) {
            itemsToChange = Arrays.asList((Item) itemHandler.getEquippedWeapon(character));
        } else {
            itemsToChange = itemHandler.getItems(character, command.getId());
        }
        return itemsToChange;
    }

}
