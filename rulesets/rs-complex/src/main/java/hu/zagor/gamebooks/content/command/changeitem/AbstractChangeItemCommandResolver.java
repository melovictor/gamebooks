package hu.zagor.gamebooks.content.command.changeitem;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import java.lang.reflect.Field;
import java.util.List;
import org.mvel2.MVEL;
import org.springframework.util.ReflectionUtils;

/**
 * Main bean for resolving an item altering command.
 * @author Tamas_Szekeres
 * @param <C> the {@link Character} type
 * @param <H> the {@link CharacterItemHandler} type
 * @param <I> the {@link Item} type
 */
public abstract class AbstractChangeItemCommandResolver<C extends Character, H extends CharacterItemHandler, I extends Item>
    extends TypeAwareCommandResolver<ChangeItemCommand> {

    @SuppressWarnings("unchecked")
    @Override
    protected List<ParagraphData> doResolve(final ChangeItemCommand command, final ResolvationData resolvationData) {
        final C character = (C) resolvationData.getCharacter();
        final H itemHandler = (H) resolvationData.getCharacterHandler().getItemHandler();
        final List<Item> items = getItemsToChange(command, character, itemHandler);
        for (final Item item : items) {
            final Field field = ReflectionUtils.findField(item.getClass(), command.getAttribute());
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

    abstract List<Item> getItemsToChange(final ChangeItemCommand command, final C character, final H itemHandler);

}
