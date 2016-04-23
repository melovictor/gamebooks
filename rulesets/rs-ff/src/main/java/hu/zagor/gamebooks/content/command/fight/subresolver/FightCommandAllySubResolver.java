package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandExecuter;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class for resolving the {@link FightCommand} object in the basic mode.
 * @author Tamas_Szekeres
 */
public class FightCommandAllySubResolver extends AbstractFightCommandSubResolver {

    @Autowired private CommandExecuter immediateCommandExecuter;

    @Override
    protected void prepareLuckTest(final FightCommand command, final FfCharacter character, final FfUserInteractionHandler interactionHandler) {
        command.setLuckOnHit(false);
        command.setLuckOnDefense(false);
    }

    @Override
    protected FfCharacter resolveCharacter(final FightCommand command, final ResolvationData resolvationData) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        FfAllyCharacter liveAlly = null;
        for (final FfAllyCharacter ally : command.getResolvedAllies()) {
            if (liveAlly == null || !attributeHandler.isAlive(liveAlly)) {
                liveAlly = ally;
            }
        }
        return liveAlly;
    }

    @Override
    protected void resolveBattlingParties(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        super.resolveBattlingParties(command, resolvationData, resolveList);
        final List<FfEnemy> allies = collectEnemies(command.getRoundNumber(), command.getAllies(), resolvationData.getEnemies());
        final List<FfAllyCharacter> resolvedAllies = new ArrayList<>();
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

        if (!command.isOngoing()) {
            resurrectAllies(command.getAllies(), resolvationData.getEnemies());
        }
    }

    private void resurrectAllies(final List<String> allies, final Map<String, Enemy> enemies) {
        for (final String allyId : allies) {
            final FfEnemy enemy = (FfEnemy) enemies.get(allyId);
            if (enemy.isResurrectable()) {
                enemy.setStamina(enemy.getInitialStamina());
            }
        }
    }

    @Override
    protected boolean aliveAfterResolvation(final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        if (resolveList != null) {
            for (final ParagraphData dataObject : resolveList) {
                immediateCommandExecuter.execute(resolvationData, (FfParagraphData) dataObject);
            }
        }

        boolean isAlive = false;
        final FightCommand fightCommand = (FightCommand) resolvationData.getParagraph().getItemsToProcess().get(0).getCommand();
        for (final FfAllyCharacter ally : fightCommand.getResolvedAllies()) {
            isAlive |= ally.getStamina() > 0;
        }
        return isAlive;
    }

    @Override
    void executeBattle(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        if (command.getResolvedAllies().size() > 1) {
            for (final FfAllyCharacter ally : command.getResolvedAllies()) {
                if (isAnyAlive(command.getResolvedEnemies())) {
                    super.executeBattle(command, provideNewResolvationData(resolvationData, ally), beforeRoundResult);
                }
            }
        } else {
            super.executeBattle(command, resolvationData, beforeRoundResult);
        }
    }

    private boolean isAnyAlive(final List<FfEnemy> resolvedEnemies) {
        boolean isAnyAlive = false;
        for (final FfEnemy enemy : resolvedEnemies) {
            isAnyAlive |= enemy.getStamina() > 0;
        }
        return isAnyAlive;
    }

    private ResolvationData provideNewResolvationData(final ResolvationData resolvationData, final FfAllyCharacter ally) {
        return DefaultResolvationDataBuilder.builder().usingResolvationData(resolvationData).withCharacter(ally).build();
    }

}
