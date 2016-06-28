package hu.zagor.gamebooks.content.command.userinput;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * User input command resolver for the Sorcery series.
 * @author Tamas_Szekeres
 */
public class SorUserInputCommandResolver extends TypeAwareCommandResolver<UserInputCommand> {
    private static final int LAST_VALUE_MODULUS = 10;
    @Resource(name = "sorGoblinGeneratorLocations") private Set<String> goblinGeneratorLocations;
    @Autowired @Qualifier("userInputCommandResolver") private TypeAwareCommandResolver<UserInputCommand> decorated;

    @Override
    public List<ParagraphData> doResolve(final UserInputCommand command, final ResolvationData resolvationData) {
        final CommandResolveResult resolve = decorated.resolve(command, resolvationData);

        if (askingForGoblinTeeth(resolvationData)) {
            changeMaxEntryValue(command, resolvationData);
        }

        return resolve.getResolveList();
    }

    private void changeMaxEntryValue(final UserInputCommand command, final ResolvationData resolvationData) {
        final CharacterItemHandler itemHandler = resolvationData.getInfo().getCharacterHandler().getItemHandler();
        final List<Item> items = itemHandler.getItems(resolvationData.getCharacter(), "3201");
        final int currentMax = command.getMax();
        final int count = count(items);
        command.setMax(Math.min(count, currentMax));
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
        final String section = resolvationData.getParagraph().getId();
        return goblinGeneratorLocations.contains(bookId + "-" + section);
    }

}
