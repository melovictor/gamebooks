package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.changeenemy.ChangeEnemyCommand;
import hu.zagor.gamebooks.content.command.changeenemy.ChangeEnemyCommandResolver;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class for resolving the {@link FightCommand} object in the basic mode.
 * @author Tamas_Szekeres
 */
public class FightCommandAllySubResolver extends AbstractFightCommandSubResolver {

    @Autowired
    private ChangeEnemyCommandResolver changeEnemyResolver;

    @Override
    protected void prepareLuckTest(final FightCommand command, final FfCharacter character, final FfUserInteractionHandler interactionHandler) {
        command.setLuckOnHit(false);
        command.setLuckOnDefense(false);
    }

    @Override
    protected FfCharacter resolveCharacter(final FightCommand command, final ResolvationData resolvationData) {
        return command.getResolvedAllies().get(0);
    }

    @Override
    protected void resolveBattlingParties(final FightCommand command, final ResolvationData resolvationData) {
        super.resolveBattlingParties(command, resolvationData);
        final List<FfEnemy> allies = collectEnemies(command.getRoundNumber(), command.getAllies(), resolvationData.getEnemies());
        final List<FfCharacter> resolvedAllies = new ArrayList<>();
        for (final FfEnemy ally : allies) {
            final FfAllyCharacter allyCharacter = new FfAllyCharacter(ally);
            allyCharacter.setName(ally.getName());
            allyCharacter.getUserInteraction().putAll(resolvationData.getCharacter().getUserInteraction());
            final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
            itemHandler.addItem(allyCharacter, "allyWeapon", 1);
            itemHandler.getEquippedWeapon(allyCharacter);
            resolvedAllies.add(allyCharacter);
        }
        command.getResolvedAllies().clear();
        command.getResolvedAllies().addAll(resolvedAllies);
    }

    @Override
    protected boolean aliveAfterResolvation(final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        if (resolveList != null) {
            for (final ParagraphData dataObject : resolveList) {
                final FfParagraphData data = (FfParagraphData) dataObject;
                if (data != null) {
                    final CommandList immediateCommands = data.getImmediateCommands();
                    if (immediateCommands != null) {
                        final Iterator<Command> iterator = immediateCommands.iterator();
                        while (iterator.hasNext()) {
                            final Command commandObject = iterator.next();
                            if (commandObject instanceof ChangeEnemyCommand) {
                                changeEnemyResolver.resolve(commandObject, resolvationData);
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }

        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        return attributeHandler.isAlive(character);
    }
}
