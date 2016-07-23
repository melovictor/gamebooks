package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.UserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link FightRoundResult} interface for resolving single fight rounds in SOR3.
 * @author Tamas_Szekeres
 */
@Component("singlesor3FightRoundResolver")
public class SingleSor3FightRoundResolver extends SingleFightRoundResolver {

    private static final int AIR_SERPENT_FORCED_DAMAGE = 3;
    private static final int AIR_SERPENT_BASE_DAMAGE_ABSORPTION = 10;
    private static final String AIR_SERPENT_ID = "44";
    private static final int ANGERED_BADDU_BUG_ATTACK_STRENGTH_BONUS = 4;
    @Autowired private MessageSource source;
    @Autowired private LocaleProvider provider;
    @Resource(name = "sor3Snattacats") private Set<String> snattaCat;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        if (fightingWithAirSerpent(command)) {
            prepareAttackDefenseValues(resolvationData);
        }

        final FightRoundResult[] resolveRound = superResolveRound(command, resolvationData, beforeRoundResult);

        if (centaurReadyToSurrender(command, resolvationData, resolveRound)) {
            command.setFleeAllowed(true);
            final FightFleeData fleeData = new FightFleeData();
            fleeData.setAfterRound(1);
            fleeData.setSufferDamage(false);
            fleeData.setText(source.getMessage("page.sor3.fight.centaur.flee", null, provider.getLocale()));
            command.setFleeData(fleeData);
        } else if (command.getEnemies().contains("8")) {
            if (resolvationData.getCharacter().getParagraphs().contains("488")) {
                final FfEnemy enemy = (FfEnemy) resolvationData.getEnemies().get("8");
                enemy.setAttackStrengthBonus(ANGERED_BADDU_BUG_ATTACK_STRENGTH_BONUS);
            }
        } else if (notLastSnattacatJustDied(command, resolvationData)) {
            resolvationData.getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "4008", 2);
        } else if (fightingWithAirSerpent(command)) {
            storeSerpentRoundResult(resolveRound[0], resolvationData);
        }

        return resolveRound;
    }

    FightRoundResult[] superResolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        return super.resolveRound(command, resolvationData, beforeRoundResult);
    }

    private void storeSerpentRoundResult(final FightRoundResult resolveRound, final ResolvationData resolvationData) {
        final SorCharacter character = (SorCharacter) resolvationData.getCharacter();
        final UserInteractionHandler interactionHandler = resolvationData.getCharacterHandler().getInteractionHandler();
        final String lastRoundResult = interactionHandler.getInteractionState(character, "lastRound");

        if (!("WIN".equals(lastRoundResult) && resolveRound == FightRoundResult.WIN)) {
            interactionHandler.setInteractionState(character, "lastRound", resolveRound.name());
        }
    }

    private void prepareAttackDefenseValues(final ResolvationData resolvationData) {
        final SorCharacter character = (SorCharacter) resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final UserInteractionHandler interactionHandler = characterHandler.getInteractionHandler();
        final String lastRoundResult = interactionHandler.peekInteractionState(character, "lastRound");
        final FfEnemy airSerpent = (FfEnemy) resolvationData.getEnemies().get(AIR_SERPENT_ID);
        if ("WIN".equals(lastRoundResult)) {
            airSerpent.setStaminaDamage(1);
            final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) characterHandler.getItemHandler();
            final FfItem equippedWeapon = itemHandler.getEquippedWeapon(character);
            airSerpent.setDamageAbsorption(equippedWeapon.getStaminaDamage() - AIR_SERPENT_FORCED_DAMAGE);
        } else if ("LOSE".equals(lastRoundResult)) {
            airSerpent.setStaminaDamage(airSerpent.getStaminaDamage() * 2);
            airSerpent.setDamageAbsorption(AIR_SERPENT_BASE_DAMAGE_ABSORPTION);
        } else {
            airSerpent.setStaminaDamage(1);
            airSerpent.setDamageAbsorption(AIR_SERPENT_BASE_DAMAGE_ABSORPTION);
        }
    }

    private boolean fightingWithAirSerpent(final FfFightCommand command) {
        return command.getEnemies().contains(AIR_SERPENT_ID);
    }

    private boolean notLastSnattacatJustDied(final FfFightCommand command, final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final String enemyId = interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
        final FfEnemy enemy = (FfEnemy) resolvationData.getEnemies().get(enemyId);
        return isSnattaCat(enemyId) && isDead(enemy) && command.getResolvedEnemies().size() > 1;
    }

    private boolean isSnattaCat(final String enemyId) {
        return snattaCat.contains(enemyId);
    }

    private boolean centaurReadyToSurrender(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] resolveRound) {
        boolean willigToSurrender = false;
        final List<String> enemies = command.getEnemies();
        final String firstId = enemies.contains("5") ? "5" : (enemies.contains("9") ? "9" : null);
        if (firstId != null) {
            int dead = enemies.size();
            Enemy alive = null;
            final Map<String, Enemy> enemyMap = resolvationData.getEnemies();
            if (!isDead(enemyMap.get(firstId))) {
                dead--;
                alive = enemyMap.get(firstId);
            }
            if (!isDead(enemyMap.get("6"))) {
                dead--;
                alive = enemyMap.get("6");
            }
            if (!isDead(enemyMap.get("7"))) {
                dead--;
                alive = enemyMap.get("7");
            }
            if (dead == 2 && alive != null) {
                final String enemyId = alive.getId();
                final BattleStatistics battleStatistics = command.getBattleStatistics(enemyId);
                willigToSurrender = (battleStatistics.getTotalWin() + (resolveRound[0] == FightRoundResult.WIN ? 1 : 0)) > 1;
            }
        }
        return willigToSurrender;
    }

    private boolean isDead(final Enemy enemy) {
        final FfEnemy ffEnemy = (FfEnemy) enemy;
        return ffEnemy.getStamina() < 1;
    }

    @Override
    public void resolveFlee(final FfFightCommand command, final ResolvationData resolvationData) {
        super.resolveFlee(command, resolvationData);
    }

}
