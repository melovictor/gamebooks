package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.subresolver.domain.LuckTestSettings;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for resolving the {@link FightCommand} object in the basic mode.
 * @author Tamas_Szekeres
 */
public class FightCommandSupportedSubResolver extends FightCommandBasicSubResolver {

    /**
     * Turns off the luck settings.
     * @param command the {@link FightCommand} object
     * @return the previous settings
     */
    protected LuckTestSettings prepareAllyLuckTest(final FightCommand command) {
        return prepareAllyLuckTest(command, new LuckTestSettings(false, false));
    }

    /**
     * Sets the luck settings to the specified values.
     * @param command the {@link FightCommand} object
     * @param luckTestSettings the values to which the luck settings must be set
     * @return the previous settings
     */
    protected LuckTestSettings prepareAllyLuckTest(final FightCommand command, final LuckTestSettings luckTestSettings) {
        final LuckTestSettings originalSettings = new LuckTestSettings(command.isLuckOnHit(), command.isLuckOnDefense());
        command.setLuckOnHit(luckTestSettings.isOnHit());
        command.setLuckOnDefense(luckTestSettings.isOnDefense());
        return originalSettings;
    }

    @Override
    protected FfCharacter resolveCharacter(final FightCommand command, final ResolvationData resolvationData) {
        return (FfCharacter) resolvationData.getCharacter();
    }

    @Override
    protected void resolveBattlingParties(final FightCommand command, final ResolvationData resolvationData) {
        super.resolveBattlingParties(command, resolvationData);
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
    }

    @Override
    void executeBattle(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        command.setKeepOpen(true);
        final FightRoundResolver roundResolver = getBeanFactory().getBean(command.getBattleType() + "FightRoundResolver", FightRoundResolver.class);
        final FightRoundResult[] roundResults = roundResolver.resolveRound(command, resolvationData, beforeRoundResult);
        updateBattleStatistics(command, roundResults);
        final LuckTestSettings luckTestSettings = prepareAllyLuckTest(command);
        for (final FfCharacter ally : command.getResolvedAllies()) {
            if (!getEnemyStatusEvaluator().enemiesAreDead(command.getResolvedEnemies(), command.getRoundNumber())) {
                final ResolvationData newResolvationData = new ResolvationData(resolvationData.getRootData(), ally, resolvationData.getEnemies(),
                    resolvationData.getInfo());
                roundResolver.resolveRound(command, newResolvationData, beforeRoundResult);
            }
        }
        prepareAllyLuckTest(command, luckTestSettings);
    }

}
