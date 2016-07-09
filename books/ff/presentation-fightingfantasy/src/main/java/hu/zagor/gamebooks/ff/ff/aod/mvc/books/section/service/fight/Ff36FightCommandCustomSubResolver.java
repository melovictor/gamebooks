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
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.subresolver.FightCommandSubResolver;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
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

    private static final int SCOUT_PARTY_SIZE = 10;
    private static final int PRISONERS_FREED = 10;

    @Autowired @Qualifier("customFf36FightRoundResolver") private FightRoundResolver roundResolver;

    @Override
    public List<ParagraphData> doResolve(final FightCommand command, final ResolvationData resolvationData) {
        List<ParagraphData> result = null;
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) characterHandler.getInteractionHandler();
        final Ff36Character character = (Ff36Character) resolvationData.getCharacter();
        final String order = interactionHandler.peekLastFightCommand(character);
        if (order == null) {
            setUpOwnFightParty(command, resolvationData);
        } else if (FightCommand.ATTACKING.equals(order)) {
            result = handleAttacking(command, resolvationData);
        } else {
            result = handleLosingArmy(command, characterHandler, character);
        }

        return result;
    }

    private List<ParagraphData> handleAttacking(final FightCommand command, final ResolvationData resolvationData) {
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
                    removeDeadSoldiers(command.getResolvedAllies().get(0), -command.getResolvedAllies().get(1).getStamina());
                } else {
                    command.setBattleType("custom36SelectDead");
                }
            } else {
                command.setOngoing(false);
                result = Arrays.asList((ParagraphData) command.getWin().get(0).getParagraphData());
            }
        }
        return result;
    }

    private List<ParagraphData> handleLosingArmy(final FightCommand command, final CharacterHandler characterHandler, final Ff36Character character) {
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

    private void removeDeadSoldiers(final FfAllyCharacter ally, final int amount) {
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

    private void setUpOwnFightParty(final FightCommand command, final ResolvationData resolvationData) {
        final Ff36Character character = (Ff36Character) resolvationData.getCharacter();
        final List<FfAllyCharacter> allies = command.getResolvedAllies();

        if (command.getAllies().isEmpty()) {
            setUpTotal(character, allies);
        } else {
            setUpScoutParty(command.getAllies(), resolvationData, allies);
        }
        allies.add(new Ff36AllyCharacter("losses", 0));
        setUpEnemies(command, resolvationData);
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

    private void setUpEnemies(final FightCommand command, final ResolvationData resolvationData) {
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        for (final String enemyId : command.getEnemies()) {
            final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
            if (enemy.getStamina() > 0) {
                resolvedEnemies.add(enemy);
            }
        }
    }

    private void setUpTotal(final Ff36Character character, final List<FfAllyCharacter> allies) {
        // TODO: extend when new squadron is added
        addSquadron(allies, "warriors", character.getWarriors());
        addSquadron(allies, "dvarwes", character.getDwarves());
        addSquadron(allies, "elves", character.getElves());
        addSquadron(allies, "knights", character.getKnights());
        addSquadron(allies, "wilders", character.getWilders());
        addSquadron(allies, "northerns", character.getNortherns());
    }

    private void addSquadron(final List<FfAllyCharacter> allies, final String name, final int amount) {
        if (amount > 0) {
            allies.add(new Ff36AllyCharacter(name, amount));
        }
    }

}
