package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link FightRoundResult} interface for resolving single fight rounds in SOR3.
 * @author Tamas_Szekeres
 */
@Component("singlesor3FightRoundResolver")
public class SingleSor3FightRoundResolver extends SingleFightRoundResolver {

    private static final int ANGERED_BADDU_BUG_ATTACK_STRENGTH_BONUS = 4;
    @Autowired private MessageSource source;
    @Autowired private LocaleProvider provider;
    @Autowired @Qualifier("sorHeroAttackStrengthRoller") private HeroAttackStrengthRoller heroAttackStrengthRoller;
    @Resource(name = "sor3Snattacats") private Set<String> snattaCat;

    @Override
    int[] getSelfAttackStrength(final FfCharacter character, final FightCommand command, final FfAttributeHandler attributeHandler) {
        return heroAttackStrengthRoller.getSelfAttackStrength(character, command, attributeHandler);
    }

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FightRoundResult[] resolveRound = super.resolveRound(command, resolvationData, beforeRoundResult);

        if (centaurReadyToSurrender(command, resolvationData, resolveRound)) {
            command.setFleeAllowed(true);
            final FightFleeData fleeData = new FightFleeData();
            fleeData.setAfterRound(1);
            fleeData.setSufferDamage(false);
            fleeData.setText(source.getMessage("page.sor3.fight.centaur.flee", null, provider.getLocale()));
            command.setFleeData(fleeData);
        } else if (command.getEnemies().contains("8")) {
            final FfEnemy enemy = (FfEnemy) resolvationData.getEnemies().get("8");
            if (resolvationData.getCharacter().getParagraphs().contains("488")) {
                enemy.setAttackStrengthBonus(ANGERED_BADDU_BUG_ATTACK_STRENGTH_BONUS);
            }
        } else if (notLastSnattacatJustDied(command, resolvationData)) {
            resolvationData.getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "4008", 2);
        }

        return resolveRound;
    }

    private boolean notLastSnattacatJustDied(final FightCommand command, final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final String enemyId = interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
        final FfEnemy enemy = (FfEnemy) resolvationData.getEnemies().get(enemyId);
        return isSnattaCat(enemyId) && isDead(enemy) && command.getResolvedEnemies().size() > 1;
    }

    private boolean isSnattaCat(final String enemyId) {
        return snattaCat.contains(enemyId);
    }

    private boolean centaurReadyToSurrender(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] resolveRound) {
        boolean willigToSurrender = false;
        final List<String> enemies = command.getEnemies();
        final String firstId = enemies.contains("5") ? "5" : (enemies.contains("9") ? "9" : null);
        if (firstId != null) {
            int dead = enemies.size();
            Enemy alive = null;
            if (!isDead(resolvationData.getEnemies().get(firstId))) {
                dead--;
                alive = resolvationData.getEnemies().get(firstId);
            }
            if (!isDead(resolvationData.getEnemies().get("6"))) {
                dead--;
                alive = resolvationData.getEnemies().get("6");
            }
            if (!isDead(resolvationData.getEnemies().get("7"))) {
                dead--;
                alive = resolvationData.getEnemies().get("7");
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
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        super.resolveFlee(command, resolvationData);
    }

}
