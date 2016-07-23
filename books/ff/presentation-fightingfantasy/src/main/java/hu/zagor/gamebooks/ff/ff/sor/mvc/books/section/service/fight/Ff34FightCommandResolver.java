package hu.zagor.gamebooks.ff.ff.sor.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.AttributeHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.CommandResolver;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.FfFightCommandResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Fight command resolver for FF34.
 * @author Tamas_Szekeres
 */
public class Ff34FightCommandResolver implements CommandResolver {
    @Autowired @Qualifier("fightCommandResolver") private FfFightCommandResolver decorated;

    @Override
    public CommandResolveResult resolve(final Command command, final ResolvationData resolvationData) {
        if (initializationRound(resolvationData)) {
            storeInitialStamina(resolvationData);
        }

        final CommandResolveResult resolve = decorated.resolve(command, resolvationData);

        if (endOfBattle((FfFightCommand) command) && hasMagicSword(resolvationData)) {
            restoreSingleStamina(resolvationData);
        }

        return resolve;
    }

    private void restoreSingleStamina(final ResolvationData resolvationData) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final int startStamina = Integer.valueOf(characterHandler.getInteractionHandler().getLastFightCommand(character, "startStamina"));
        final int currentStamina = characterHandler.getAttributeHandler().resolveValue(character, "stamina");
        character.changeStamina(Math.min(1, startStamina - currentStamina));
    }

    private boolean hasMagicSword(final ResolvationData resolvationData) {
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        return itemHandler.hasEquippedItem(resolvationData.getCharacter(), "1002");
    }

    private boolean endOfBattle(final FfFightCommand command) {
        return !command.isOngoing();
    }

    private void storeInitialStamina(final ResolvationData resolvationData) {
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final AttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final Character character = resolvationData.getCharacter();
        final int stamina = attributeHandler.resolveValue(character, "stamina");
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) characterHandler.getInteractionHandler();
        interactionHandler.setFightCommand(character, "startStamina", String.valueOf(stamina));
    }

    private boolean initializationRound(final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        return interactionHandler.peekLastFightCommand(resolvationData.getCharacter()) == null;
    }

}
