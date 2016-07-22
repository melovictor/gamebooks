package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.util.Assert;

/**
 * Command resolver for checking items availability with the current character.
 * @author Tamas_Szekeres
 */
public class ItemCheckCommandResolver extends TypeAwareCommandResolver<ItemCheckCommand> {
    @LogInject private Logger logger;
    private Map<String, ItemCheckStubCommandResolver> stubCommands;

    @Override
    protected CommandResolveResult doResolveWithResolver(final ItemCheckCommand command, final ResolvationData resolvationData) {
        final CommandResolveResult result = super.doResolveWithResolver(command, resolvationData);
        result.setFinished(true);
        return result;
    }

    @Override
    public List<ParagraphData> doResolve(final ItemCheckCommand command, final ResolvationData resolvationData) {
        Assert.notNull(resolvationData, "The parameter 'resolvationData' cannot be null!");

        Assert.state(stubCommands != null, "The field 'stubCommands' must be set!");
        Assert.state(command.getCheckType() != null, "The field 'stubCommands' must be set!");
        logger.info("Checking availability of {} '{}'.", command.getCheckType(), command.getId());

        ParagraphData toResolve;
        final ItemCheckStubCommandResolver stubCommand = stubCommands.get(command.getCheckType());
        if (stubCommand != null) {
            toResolve = stubCommand.resolve(command, resolvationData);
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

    public void setStubCommands(final Map<String, ItemCheckStubCommandResolver> stubCommands) {
        this.stubCommands = stubCommands;
    }

}
