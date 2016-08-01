package hu.zagor.gamebooks.ff.ff.aod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.AttributeHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.subresolver.FightCommandSubResolver;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.aod.character.Ff36Character;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Custom sub resolver for FF36 for the army battles.
 * @author Tamas_Szekeres
 */
public class Ff36FightCommandCustomSubResolver implements FightCommandSubResolver {

    @Autowired @Qualifier("customFf36FightRoundResolver") private FfFightRoundResolver roundResolver;

    @Override
    public List<ParagraphData> doResolve(final FfFightCommand command, final ResolvationData resolvationData) {
        List<ParagraphData> result;
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) characterHandler.getInteractionHandler();
        final Ff36Character character = (Ff36Character) resolvationData.getCharacter();
        final String order = interactionHandler.getLastFightCommand(character);
        if (order == null) {
            result = setUpFightParties(command, resolvationData);
        } else if (FfFightCommand.ATTACKING.equals(order)) {
            result = handleAttacking(command, resolvationData);
        } else {
            result = handleLosingArmy(command, characterHandler, character);
        }

        return result;
    }

    private List<ParagraphData> handleAttacking(final FfFightCommand command, final ResolvationData resolvationData) {
        List<ParagraphData> result = null;
        roundResolver.resolveRound(command, resolvationData, null);
        if (weAreDead(command.getResolvedAllies())) {
            result = Arrays.asList((ParagraphData) command.getLose());
            command.setOngoing(false);
            removeDeadSoldiers(command.getResolvedAllies(), resolvationData);
        } else if (enemyDead(command.getResolvedEnemies())) {
            final FfAllyCharacter losses = command.getResolvedAllies().get(command.getResolvedAllies().size() - 1);
            if (losses.getStamina() != 0) {
                if (command.getResolvedAllies().size() == 2) {
                    command.setOngoing(false);
                    result = Arrays.asList((ParagraphData) command.getWin().get(0).getParagraphData());
                    removeDeadSoldiers(command, resolvationData);
                } else {
                    command.setBattleType("ff36customSelectDead");
                }
            } else {
                command.setOngoing(false);
                result = Arrays.asList((ParagraphData) command.getWin().get(0).getParagraphData());
            }
        }
        return result;
    }

    private List<ParagraphData> handleLosingArmy(final FfFightCommand command, final CharacterHandler characterHandler, final Ff36Character character) {
        List<ParagraphData> result;
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) characterHandler.getInteractionHandler();
        final String fightersToLose = interactionHandler.getLastFightCommand(character, "loseArmy");
        final String[] loseOrders = fightersToLose.split(";");
        for (final String loseOrder : loseOrders) {
            final String[] toLose = loseOrder.split("=");
            final FfAttributeHandler attributeHandler = (FfAttributeHandler) characterHandler.getAttributeHandler();
            attributeHandler.handleModification(character, toLose[0], -Integer.parseInt(toLose[1]));
        }
        command.setOngoing(false);
        result = Arrays.asList((ParagraphData) command.getWin().get(0).getParagraphData());
        return result;
    }

    private void removeDeadSoldiers(final FfFightCommand command, final ResolvationData resolvationData) {
        final FfEnemy ally = (FfEnemy) resolvationData.getEnemies().get(command.getAllies().get(0));
        final int amount = -command.getResolvedAllies().get(1).getStamina();

        final FfAttributeHandler attributeHandler = (FfAttributeHandler) resolvationData.getCharacterHandler().getAttributeHandler();
        attributeHandler.handleModification((FfCharacter) resolvationData.getCharacter(), ally.getName(), -amount);
        ally.setStamina(ally.getStamina() - amount);
    }

    private void removeDeadSoldiers(final List<FfAllyCharacter> list, final ResolvationData resolvationData) {
        final Ff36Character character = (Ff36Character) resolvationData.getCharacter();
        final FfAttributeHandler attributeHandler = (FfAttributeHandler) resolvationData.getCharacterHandler().getAttributeHandler();
        for (final FfAllyCharacter ally : list) {
            if (ally.getStamina() > 0) {
                attributeHandler.handleModification(character, ally.getName(), -ally.getStamina());
            }
        }
    }

    private boolean weAreDead(final List<FfAllyCharacter> list) {
        int totalAlive = 0;
        for (final FfAllyCharacter ally : list) {
            totalAlive += ally.getStamina();
        }
        return totalAlive <= 0;
    }

    private boolean enemyDead(final List<FfEnemy> list) {
        int totalAlive = 0;
        for (final FfEnemy enemy : list) {
            totalAlive += enemy.getStamina();
        }
        return totalAlive <= 0;
    }

    private List<ParagraphData> setUpFightParties(final FfFightCommand command, final ResolvationData resolvationData) {
        List<ParagraphData> result = null;
        final Ff36Character character = (Ff36Character) resolvationData.getCharacter();
        final List<FfAllyCharacter> allies = command.getResolvedAllies();

        if (command.getAllies().isEmpty()) {
            setUpTotal(character, allies);
        } else {
            setUpScoutParty(command.getAllies(), resolvationData, allies);
        }
        allies.add(new Ff36AllyCharacter("losses", 0));
        setUpEnemies(command, resolvationData);
        if (enemyDead(command.getResolvedEnemies())) {
            command.setKeepOpen(false);
            command.setOngoing(false);
            result = Arrays.asList((ParagraphData) command.getWin().get(0).getParagraphData());
        }
        return result;
    }

    private void setUpScoutParty(final List<String> allyIds, final ResolvationData resolvationData, final List<FfAllyCharacter> resolvedAllies) {
        final AttributeHandler attributeHandler = resolvationData.getCharacterHandler().getAttributeHandler();
        final Character character = resolvationData.getCharacter();
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        for (final String allyId : allyIds) {
            final FfEnemy enemy = (FfEnemy) enemies.get(allyId);
            final String name = enemy.getName();
            addSquadron(resolvedAllies, name, Math.min(enemy.getStamina(), attributeHandler.resolveValue(character, name)));
        }
    }

    private void setUpEnemies(final FfFightCommand command, final ResolvationData resolvationData) {
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        for (final String enemyId : command.getEnemies()) {
            final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
            final int stamina = enemy.getStamina();
            if (stamina > 0) {
                resolvedEnemies.add(enemy);
                final int initStamina = enemy.getInitialStamina();
                if (stamina > initStamina) {
                    enemy.setStamina(initStamina);
                }
            }
        }
    }

    private void setUpTotal(final Ff36Character character, final List<FfAllyCharacter> allies) {
        addSquadron(allies, "warriors", character.getWarriors());
        addSquadron(allies, "dwarves", character.getDwarves());
        addSquadron(allies, "elves", character.getElves());
        addSquadron(allies, "knights", character.getKnights());
        addSquadron(allies, "wilders", character.getWilders());
        addSquadron(allies, "northerns", character.getNortherns());
        addSquadron(allies, "marauders", character.getMarauders());
        addSquadron(allies, "whiteKnights", character.getWhiteKnights());
    }

    private void addSquadron(final List<FfAllyCharacter> allies, final String name, final int amount) {
        if (amount > 0) {
            allies.add(new Ff36AllyCharacter(name, amount));
        }
    }

}
