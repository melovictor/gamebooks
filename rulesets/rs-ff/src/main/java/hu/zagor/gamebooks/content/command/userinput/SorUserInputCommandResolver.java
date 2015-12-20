package hu.zagor.gamebooks.content.command.userinput;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.ParagraphData;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;

/**
 * User input command resolver for the Sorcery series.
 * @author Tamas_Szekeres
 */
public class SorUserInputCommandResolver extends UserInputCommandResolver {
    private static final int LAST_VALUE_MODULUS = 10;
    @Resource(name = "sorGoblinGeneratorLocations") private Set<String> goblinGeneratorLocations;

    @Override
    public List<ParagraphData> doResolve(final UserInputCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> doResolve = super.doResolve(command, resolvationData);

        if (askingForGoblinTeeth(resolvationData)) {
            changeMaxEntryValue(command, resolvationData);
        }

        return doResolve;
    }

    private void changeMaxEntryValue(final UserInputCommand command, final ResolvationData resolvationData) {
        final CharacterItemHandler itemHandler = resolvationData.getInfo().getCharacterHandler().getItemHandler();
        final List<Item> items = itemHandler.getItems(resolvationData.getCharacter(), "3201");
        final int count = count(items);
        command.setMax(count);
    }

    private int count(final List<Item> items) {
        int count = 0;
        for (final Item item : items) {
            count += item.getAmount();
        }
        return count;
    }

    private boolean askingForGoblinTeeth(final ResolvationData resolvationData) {
        final Long bookId = resolvationData.getInfo().getId() % LAST_VALUE_MODULUS;
        final String section = resolvationData.getSection();
        return goblinGeneratorLocations.contains(bookId + "-" + section);
    }

}
