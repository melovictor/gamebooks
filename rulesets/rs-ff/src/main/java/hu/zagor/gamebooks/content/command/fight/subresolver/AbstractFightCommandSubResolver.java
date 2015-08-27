package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightBoundingCommandResolver;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightCommandRoundEventResolver;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.subresolver.autolose.AutoLoseHandler;
import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.EnemyStatusEvaluator;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract class for the {@link FightCommandSubResolver} containing the common parts for the resolvation.
 * @author Tamas_Szekeres
 */
public abstract class AbstractFightCommandSubResolver implements FightCommandSubResolver, BeanFactoryAware {

    private BeanFactory beanFactory;
    @Autowired
    private FightCommandRoundEventResolver roundEventResolver;
    @Autowired
    private FightBoundingCommandResolver fightBoundingCommandResolver;
    @Autowired
    private FightCommandBeforeEventResolver beforeEventResolver;
    @Autowired
    private AutoLoseHandler autoLoseHandler;
    @Autowired
    private EnemyStatusEvaluator enemyStatusEvaluator;

    @Override
    public List<ParagraphData> doResolve(final FightCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> resolveList = new ArrayList<>();
        resolveBattlingParties(command, resolvationData);
        final FfCharacter battlingCharacter = resolveCharacter(command, resolvationData);
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        if (!isAlive(battlingCharacter, characterHandler)) {
            loseBattle(command, resolveList);
        } else {
            if (enemyStatusEvaluator.enemiesAreDead(command.getResolvedEnemies(), command.getRoundNumber())) {
                winBattle(command, resolveList, resolvationData.getEnemies());
            } else {
                command.setOngoing(true);
                resolveBattleRound(command, resolvationData, resolveList);
            }
        }
        if (command.getFleeData() != null) {
            command.setFleeAllowed(command.getRoundNumber() >= command.getFleeData().getAfterRound());
        }
        characterHandler.getAttributeHandler().sanityCheck(battlingCharacter);
        resolveBattlingParties(command, resolvationData);
        return resolveList;
    }

    /**
     * Method to initialize the {@link FightCommand}.
     * @param command the {@link FightCommand} to initialize
     * @param resolvationData the {@link ResolvationData} to use for the initialization
     */
    protected void resolveBattlingParties(final FightCommand command, final ResolvationData resolvationData) {
        final List<FfEnemy> enemies = collectEnemies(command.getRoundNumber() + 1, command.getEnemies(), resolvationData.getEnemies());
        command.getResolvedEnemies().clear();
        command.getResolvedEnemies().addAll(enemies);
    }

    /**
     * Method to get the {@link FfCharacter} object that will fight the battle.
     * @param command the {@link FightCommand}
     * @param resolvationData the {@link ResolvationData} containing all information required for the successful resolvation
     * @return the {@link FfCharacter}
     */
    protected abstract FfCharacter resolveCharacter(FightCommand command, final ResolvationData resolvationData);

    private void resolveBattleRound(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final FfCharacter heroCharacter = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfUserInteractionHandler interactionHandler = characterHandler.getInteractionHandler();
        final String lastFightCommand = interactionHandler.getLastFightCommand(heroCharacter);
        prepareLuckTest(command, heroCharacter, interactionHandler);
        final ResolvationData battlingResolvationData = provideNewResolvationData(command, resolvationData);
        if (FightCommand.ATTACKING.equals(lastFightCommand)) {
            handleAttacking(command, battlingResolvationData, resolveList);
        } else if (FightCommand.FLEEING.equals(lastFightCommand)) {
            handleFleeing(command, battlingResolvationData, resolveList);
        } else {
            command.setOngoing(true);
        }
    }

    private ResolvationData provideNewResolvationData(final FightCommand command, final ResolvationData resolvationData) {
        final Character newCharacter = resolveCharacter(command, resolvationData);
        final Character oldCharacter = resolvationData.getCharacter();
        ResolvationData battlingResolvationData = resolvationData;
        if (newCharacter != oldCharacter) {
            battlingResolvationData = DefaultResolvationDataBuilder.builder().usingResolvationData(resolvationData).withCharacter(newCharacter).build();
        }
        return battlingResolvationData;
    }

    private void handleFleeing(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        resolveList.add(command.getFlee());
        command.setOngoing(false);
        final FightRoundResolver roundResolver = getRoundResolver(command, resolvationData.getInfo().getResourceDir());
        roundResolver.resolveFlee(command, resolvationData);
    }

    private FightRoundResolver getRoundResolver(final FightCommand command, final String resDir) {
        final String specificName = command.getBattleType() + resDir + "FightRoundResolver";
        FightRoundResolver roundResolver;
        if (beanFactory.containsBean(specificName)) {
            roundResolver = beanFactory.getBean(specificName, FightRoundResolver.class);
        } else {
            roundResolver = beanFactory.getBean(command.getBattleType() + "FightRoundResolver", FightRoundResolver.class);
        }
        return roundResolver;
    }

    /**
     * Method to handle the attacking action during a battle.
     * @param command the {@link FightCommand}
     * @param resolvationData the {@link ResolvationData}
     * @param resolveList the list containing the {@link ParagraphData} objects that need to be resolved
     */
    protected void handleAttacking(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        command.increaseBattleRound();
        command.getMessages().setRoundMessage(command.getRoundNumber());
        command.getMessages().switchToPreRoundMessages();
        final FightBeforeRoundResult beforeRoundResult = beforeEventResolver.handleBeforeRoundEvent(command, resolvationData, resolveList);
        command.getMessages().switchToRoundMessages();
        if (!beforeRoundResult.isInterrupted()) {
            executeBattle(command, resolvationData, beforeRoundResult);
            resolveBattleResult(command, resolvationData, resolveList);
            autoLoseHandler.checkAutoEvents(command, resolveList, resolvationData);
        } else {
            command.setOngoing(false);
        }
    }

    /**
     * Initializes the {@link FfUserInteractionHandler} with the luck test information the player has set up.
     * @param command the {@link FightCommand} to resolve
     * @param character the {@link FfCharacter} to store the information
     * @param interactionHandler the {@link FfUserInteractionHandler}
     */
    protected abstract void prepareLuckTest(final FightCommand command, final FfCharacter character, final FfUserInteractionHandler interactionHandler);

    private void resolveBattleResult(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final FfCharacter character = resolveCharacter(command, resolvationData);
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        command.setOngoing(false);
        if (!isAlive(character, characterHandler)) {
            loseBattle(command, resolveList);
        } else if (enemyStatusEvaluator.enemiesAreDead(command.getResolvedEnemies(), command.getRoundNumber())) {
            winBattle(command, resolveList, resolvationData.getEnemies());
        } else {
            command.setOngoing(true);
            roundEventResolver.resolveRoundEvent(command, resolveList);
            final FightRoundBoundingCommand boundingCommand = command.getAfterBounding();
            if (boundingCommand != null) {
                boundingCommand.setRoundNumber(command.getRoundNumber());
                command.getMessages().switchToPostRoundMessages();
                final List<ParagraphData> resolve = fightBoundingCommandResolver.resolve(boundingCommand, resolvationData).getResolveList();
                if (resolve != null) {
                    resolveList.addAll(resolve);
                }
            }
            if (!aliveAfterResolvation(resolvationData, resolveList)) {
                loseBattle(command, resolveList);
            }
            checkForInterruption(resolveList, command);
        }
    }

    private void checkForInterruption(final List<ParagraphData> resolveList, final FightCommand command) {
        for (final ParagraphData dataObject : resolveList) {
            final FfParagraphData data = (FfParagraphData) dataObject;
            if (data != null && data.isInterrupt()) {
                command.setOngoing(false);
            }
        }
    }

    /**
     * Execute whatever commands in the provided {@link ParagraphData} list is that is required to decide whether the hero has survived the battle or not.
     * @param resolvationData the {@link ResolvationData}
     * @param resolveList the list containing the {@link ParagraphData} that might contain some last minute changes
     * @return true if the hero is still alive, false otherwise
     */
    protected abstract boolean aliveAfterResolvation(ResolvationData resolvationData, List<ParagraphData> resolveList);

    void executeBattle(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        command.setKeepOpen(true);
        final FightRoundResolver roundResolver = getRoundResolver(command, resolvationData.getInfo().getResourceDir());
        final FightRoundResult[] roundResults = roundResolver.resolveRound(command, resolvationData, beforeRoundResult);
        updateBattleStatistics(command, roundResults);
    }

    void updateBattleStatistics(final FightCommand command, final FightRoundResult[] roundResults) {
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        for (int i = 0; i < roundResults.length; i++) {
            final FightRoundResult result = roundResults[i];
            final FfEnemy enemy = resolvedEnemies.get(i);
            command.getBattleStatistics(enemy.getId()).updateStats(result);
            command.getBattleStatistics(null).updateStats(result);
        }
    }

    /**
     * Resolves a list of enemy ids into a list of {@link FfEnemy} objects.
     * @param roundNumber the number of the upcoming round
     * @param enemyIds the ids to resolve
     * @param enemyStore the store to use for the resolvation
     * @return the list of living and resolved enemies
     */
    protected List<FfEnemy> collectEnemies(final int roundNumber, final List<String> enemyIds, final Map<String, Enemy> enemyStore) {
        final List<FfEnemy> enemies = new ArrayList<>();
        for (final String enemyId : enemyIds) {
            final FfEnemy enemy = (FfEnemy) enemyStore.get(enemyId);
            if (enemyStatusEvaluator.isAlive(enemy) && enemy.getStartAtRound() <= roundNumber) {
                enemies.add(enemy);
                if (enemy.getAlterId() != null) {
                    final FfEnemy alterEgo = (FfEnemy) enemyStore.get(enemy.getAlterId());
                    enemy.setAlterEgo(alterEgo);
                }
            }
        }
        return enemies;
    }

    private boolean isAlive(final FfCharacter character, final FfCharacterHandler characterHandler) {
        return characterHandler.getAttributeHandler().isAlive(character);
    }

    /**
     * Handles the winning of a battle.
     * @param command the {@link FightCommand} object
     * @param resolveList the list that will contain the {@link ParagraphData} objects that will have to be resolved
     * @param enemies the list of enemies
     */
    protected void winBattle(final FightCommand command, final List<ParagraphData> resolveList, final Map<String, Enemy> enemies) {
        for (final FightOutcome outcome : command.getWin()) {
            if (outcome.getMin() <= command.getRoundNumber() && outcome.getMax() >= command.getRoundNumber()) {
                resolveList.add(outcome.getParagraphData());
            }
        }
        command.setOngoing(false);
        resurrectEnemies(command, enemies);
    }

    private void loseBattle(final FightCommand command, final List<ParagraphData> resolveList) {
        resolveList.add(command.getLose());
        command.setOngoing(false);
    }

    private void resurrectEnemies(final FightCommand command, final Map<String, Enemy> enemies) {
        for (final String enemyId : command.getEnemies()) {
            final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
            if (enemy.isResurrectable()) {
                enemy.setStamina(enemy.getInitialStamina());
                enemy.setSkill(enemy.getInitialSkill());
            }
        }
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public EnemyStatusEvaluator getEnemyStatusEvaluator() {
        return enemyStatusEvaluator;
    }

}
