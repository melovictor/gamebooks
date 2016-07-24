package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.LwEnemy;
import hu.zagor.gamebooks.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.LwUserInteractionHandler;
import hu.zagor.gamebooks.content.LwParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.content.command.fight.roundresolver.LwFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.EnemyStatusEvaluator;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

/**
 * Fight command resolver for Lone Wolf.
 * @author Tamas_Szekeres
 */
public class LwFightCommandResolver extends TypeAwareCommandResolver<LwFightCommand> {
    @Autowired @Qualifier("lwEnemyStatusEvaluator") private EnemyStatusEvaluator<LwEnemy> enemyStatusEvaluator;
    @Autowired private LwFightRoundResolver roundResolver;

    @Override
    protected CommandResolveResult doResolveWithResolver(final LwFightCommand command, final ResolvationData resolvationData) {
        command.getMessages().clear();
        final CommandResolveResult result = new CommandResolveResult();
        final List<ParagraphData> resolveList = doResolve(command, resolvationData);
        result.setResolveList(resolveList);
        result.setFinished(!command.isOngoing());
        applyBattleMessages(resolvationData.getParagraph().getData(), command);
        return result;
    }

    private void applyBattleMessages(final ParagraphData rootData, final LwFightCommand command) {
        final String messages = StringUtils.collectionToDelimitedString(command.getMessages(), "<br />\n");
        if (!messages.isEmpty()) {
            final StringBuilder builder = new StringBuilder(rootData.getText() + "<p>");
            builder.append(messages);
            builder.append("</p>");
            rootData.setText(builder.toString());
        }
    }

    @Override
    protected List<ParagraphData> doResolve(final LwFightCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> resolveList = new ArrayList<>();
        resolveBattlingParties(command, resolvationData, null);
        final LwCharacter battlingCharacter = resolveCharacter(resolvationData);
        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        if (!isAlive(battlingCharacter, characterHandler)) {
            loseBattle(command, resolveList);
        } else {
            if (enemyStatusEvaluator.enemiesAreDead(command.getResolvedEnemies(), command.getRoundNumber())) {
                winBattle(command, resolveList);
            } else {
                command.setOngoing(true);
                resolveBattleRound(command, resolvationData, resolveList);
            }
        }
        if (command.getFleeData() != null) {
            command.setFleeAllowed(command.getRoundNumber() >= command.getFleeData().getAfterRound());
        }
        characterHandler.getAttributeHandler().sanityCheck(battlingCharacter);
        resolveBattlingParties(command, resolvationData, resolveList);
        return resolveList;
    }

    private void resolveBattleRound(final LwFightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final LwCharacter heroCharacter = (LwCharacter) resolvationData.getCharacter();
        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        final LwUserInteractionHandler interactionHandler = characterHandler.getInteractionHandler();
        final String lastFightCommand = interactionHandler.getLastFightCommand(heroCharacter);
        if (LwFightCommand.ATTACKING.equals(lastFightCommand)) {
            handleAttacking(command, resolvationData, resolveList);
        } else if (LwFightCommand.FLEEING.equals(lastFightCommand)) {
            handleFleeing(command, resolvationData, resolveList);
        } else {
            command.setOngoing(true);
        }
    }

    private void handleAttacking(final LwFightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        command.increaseBattleRound();
        command.getMessages().setRoundMessage(command.getRoundNumber());
        command.getMessages().switchToPreRoundMessages();
        command.getMessages().switchToRoundMessages();

        executeBattle(command, resolvationData);
        resolveBattleResult(command, resolvationData, resolveList);
    }

    private LwCharacter resolveCharacter(final ResolvationData resolvationData) {
        return (LwCharacter) resolvationData.getCharacter();
    }

    private void resolveBattleResult(final LwFightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final LwCharacter character = resolveCharacter(resolvationData);
        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        command.setOngoing(false);
        if (!isAlive(character, characterHandler)) {
            loseBattle(command, resolveList);
        } else if (enemyStatusEvaluator.enemiesAreDead(command.getResolvedEnemies(), command.getRoundNumber())) {
            winBattle(command, resolveList);
        } else {
            command.setOngoing(true);
            if (!aliveAfterResolvation(resolvationData, resolveList)) {
                loseBattle(command, resolveList);
            }
        }
    }

    private boolean aliveAfterResolvation(final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();

        final LwAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        for (final ParagraphData data : resolveList) {
            final LwParagraphData lwData = (LwParagraphData) data;
            resolveAttributeChanges(lwData, character, attributeHandler);
        }

        return attributeHandler.isAlive(character);
    }

    private void resolveAttributeChanges(final LwParagraphData lwData, final LwCharacter character, final LwAttributeHandler attributeHandler) {
        final List<ModifyAttribute> modifyAttributes = lwData.getModifyAttributes();
        for (final ModifyAttribute modify : modifyAttributes) {
            attributeHandler.handleModification(character, modify);
        }
        modifyAttributes.clear();
    }

    private void executeBattle(final LwFightCommand command, final ResolvationData resolvationData) {
        command.setKeepOpen(true);
        roundResolver.resolveRound(command, resolvationData);
    }

    private void handleFleeing(final LwFightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        resolveList.add(command.getFlee());
        command.setOngoing(false);
        roundResolver.resolveFlee(command, resolvationData);
    }

    private void resolveBattlingParties(final LwFightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        if (command.isOngoing()) {
            final List<LwEnemy> enemies = collectEnemies(command.getEnemies(), resolvationData.getEnemies());
            command.getResolvedEnemies().clear();
            command.getResolvedEnemies().addAll(enemies);
            if (resolveList != null && command.getResolvedEnemies().isEmpty()) {
                winBattle(command, resolveList);
            }
        }
    }

    private void winBattle(final LwFightCommand command, final List<ParagraphData> resolveList) {
        for (final FightOutcome outcome : command.getWin()) {
            if (outcome.getMin() <= command.getRoundNumber() && outcome.getMax() >= command.getRoundNumber()) {
                resolveList.add(outcome.getParagraphData());
            }
        }
        command.setOngoing(false);
    }

    private boolean isAlive(final LwCharacter character, final LwCharacterHandler characterHandler) {
        return characterHandler.getAttributeHandler().isAlive(character);
    }

    private void loseBattle(final LwFightCommand command, final List<ParagraphData> resolveList) {
        resolveList.add(command.getLose());
        command.setOngoing(false);
    }

    private List<LwEnemy> collectEnemies(final List<String> enemyIds, final Map<String, Enemy> enemyStore) {
        final List<LwEnemy> enemies = new ArrayList<>();
        for (final String enemyId : enemyIds) {
            final LwEnemy enemy = (LwEnemy) enemyStore.get(enemyId);
            enemies.add(enemy);
        }
        return enemies;
    }
}
