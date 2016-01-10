package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.support.logging.LogInject;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link CommandExecuter} interface that takes care of the immediate commands.
 * @author Tamas_Szekeres
 */
@Component
public class ImmediateCommandExecuter implements CommandExecuter {
    @LogInject private Logger logger;

    @Override
    public void execute(final ResolvationData resolvationData, final FfParagraphData subData) {
        if (subData != null) {
            final CommandList immediateCommands = subData.getImmediateCommands();
            if (immediateCommands != null) {
                for (final Command command : immediateCommands) {
                    logger.debug("Executing command {}.", command.toString());
                    final CommandResolver resolver = resolvationData.getInfo().getCommandResolvers().get(command.getClass());
                    resolver.resolve(command, resolvationData);
                }
            }
        }
    }

}
