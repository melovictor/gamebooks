package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Command resolver for checking items availability with the current character.
 * @author Tamas_Szekeres
 */
public class ItemCheckCommandResolver extends TypeAwareCommandResolver<ItemCheckCommand> {

    private Map<CheckType, ItemCheckStubCommand> stubCommands;

    @Override
    protected CommandResolveResult doResolveWithResolver(final ItemCheckCommand command, final ResolvationData resolvationData) {
        final CommandResolveResult result = super.doResolveWithResolver(command, resolvationData);
        result.setFinished(true);
        return result;
    }

    @Override
    public List<ParagraphData> doResolve(final ItemCheckCommand command, final ResolvationData resolvationData) {
        Assert.notNull(resolvationData, "The parameter 'resolvationData' cannot be null!");

        final ParagraphData rootDataElement = resolvationData.getRootData();
        final Character character = resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();

        Assert.notNull(rootDataElement, "The parameter 'rootDataElement' cannot be null!");
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(characterHandler, "The parameter 'characterHandler' cannot be null!");
        Assert.state(stubCommands != null, "The field 'stubCommands' must be set!");
        Assert.state(command.getCheckType() != null, "The field 'stubCommands' must be set!");

        ParagraphData toResolve;
        final ItemCheckStubCommand stubCommand = stubCommands.get(command.getCheckType());
        if (stubCommand != null) {
            toResolve = stubCommand.resolve(command, character, characterHandler);
        } else {
            throw new UnsupportedOperationException("Unknown checking type '" + command.getCheckType().toString() + "'.");
        }

        final List<ParagraphData> result = new ArrayList<>();
        if (toResolve != null) {
            result.add(toResolve);
        }
        if (command.getAfter() != null) {
            result.add(command.getAfter());
        }
        return result;
    }

    public void setStubCommands(final Map<CheckType, ItemCheckStubCommand> stubCommands) {
        this.stubCommands = stubCommands;
    }

}
