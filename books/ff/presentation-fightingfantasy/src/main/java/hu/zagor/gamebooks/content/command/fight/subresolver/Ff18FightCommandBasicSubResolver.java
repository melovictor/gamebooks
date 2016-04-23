package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * FF18-specific decorator of {@link FightCommandBasicSubResolver}.
 * @author Tamas_Szekeres
 */
public class Ff18FightCommandBasicSubResolver implements FightCommandSubResolver {

    private static final int DIRTY_TRICK = 3;
    private static final int SUDDEN_KILL_ROLL = 6;
    private List<String> proficientEnemies;
    @Autowired @Qualifier("d6RandomGenerator") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;
    @Autowired @Qualifier("fightCommandBasicSubResolver") private FightCommandBasicSubResolver superResolver;

    @Override
    public List<ParagraphData> doResolve(final FightCommand command, final ResolvationData resolvationData) {
        final boolean fightCommandIssued = fightCommandIssued(resolvationData);

        if (fightCommandIssued) {
            setUpWeapon(resolvationData);
        }
        final List<ParagraphData> doResolve = superResolver.doResolve(command, resolvationData);
        if (fightCommandIssued) {
            resetWeapon(resolvationData);
            handleDirtyTrick(command, resolvationData);

            if (fightWithFist(resolvationData) && enemyAlive(resolvationData)) {
                if (wonLastRound(command, resolvationData)) {
                    tryKillEnemy(command.getMessages(), resolvationData);
                    superResolver.resolveBattlingParties(command, resolvationData, doResolve);
                } else if (lostLastRound(command, resolvationData) && enemyProficientFighter(resolvationData)) {
                    tryKillHero(command.getMessages(), resolvationData);
                }
            }
        }

        return doResolve;
    }

    private void handleDirtyTrick(final FightCommand command, final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
        final String enemyId = getEnemyId(resolvationData);
        if ("42".equals(enemyId)) {
            if (itemHandler.hasItem(character, "4006") && command.getBattleStatistics("42").getTotalLose() == 1) {
                itemHandler.removeItem(character, "4006", 1);
                final int[] randomNumber = generator.getRandomNumber(1);
                command.getMessages().addKey("page.ff.label.random.after", renderer.render(generator.getDefaultDiceSide(), randomNumber), randomNumber[0]);
                if (randomNumber[0] > DIRTY_TRICK) {
                    itemHandler.addItem(character, "4003", 1);
                    itemHandler.addItem(character, "4007", 2);
                }
            } else {
                if (itemHandler.hasItem(character, "4007")) {
                    itemHandler.removeItem(character, "4007", 1);
                    if (!itemHandler.hasItem(character, "4007")) {
                        itemHandler.removeItem(character, "4003", 1);
                    }
                }
            }
        }
    }

    private void resetWeapon(final ResolvationData resolvationData) {
        if ("26b".equals(getEnemyId(resolvationData))) {
            final FfItem equippedWeapon = getEquippedWeapon(resolvationData);
            equippedWeapon.setStaminaDamage(2);
            equippedWeapon.setSkillDamage(0);
        }
    }

    private FfItem getEquippedWeapon(final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
        final FfItem equippedWeapon = itemHandler.getEquippedWeapon(character);
        return equippedWeapon;
    }

    private void setUpWeapon(final ResolvationData resolvationData) {
        if ("26b".equals(getEnemyId(resolvationData))) {
            final FfItem equippedWeapon = getEquippedWeapon(resolvationData);
            equippedWeapon.setStaminaDamage(0);
            equippedWeapon.setSkillDamage(1);
        }
    }

    private boolean enemyAlive(final ResolvationData resolvationData) {
        final FfEnemy enemy = getEnemy(resolvationData);
        return enemy.getStamina() > 0;
    }

    private void tryKillHero(final FightCommandMessageList fightCommandMessageList, final ResolvationData resolvationData) {
        final String enemyName = getEnemy(resolvationData).getName();
        fightCommandMessageList.positionTo("page.ff.label.fight.single.failedDefense", enemyName);

        final int[] rollValue = rollADice();
        fightCommandMessageList.addKey("page.ff18.fight.suddenDeath.roll", renderer.render(generator.getDefaultDiceSide(), rollValue), rollValue[0]);

        if (rollValue[0] == SUDDEN_KILL_ROLL) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            character.setStamina(0);
            fightCommandMessageList.addKey("page.ff18.fight.suddenDeath.hero", enemyName);
        }
    }

    private void tryKillEnemy(final FightCommandMessageList fightCommandMessageList, final ResolvationData resolvationData) {
        final FfEnemy enemy = getEnemy(resolvationData);
        final String enemyName = enemy.getName();
        fightCommandMessageList.positionTo("page.ff.label.fight.single.successfulAttack", enemyName);

        final int[] rollValue = rollADice();
        fightCommandMessageList.addKey("page.ff18.fight.suddenDeath.roll", renderer.render(generator.getDefaultDiceSide(), rollValue), rollValue[0]);

        if (rollValue[1] == SUDDEN_KILL_ROLL) {
            enemy.setStamina(0);
            fightCommandMessageList.addKey("page.ff18.fight.suddenDeath.enemy", enemyName);
        }
    }

    private FfEnemy getEnemy(final ResolvationData resolvationData) {
        final String enemyId = getEnemyId(resolvationData);
        return (FfEnemy) resolvationData.getEnemies().get(enemyId);
    }

    private int[] rollADice() {
        return generator.getRandomNumber(1);
    }

    private boolean lostLastRound(final FightCommand command, final ResolvationData resolvationData) {
        return fetchBattleStatistics(command, resolvationData).getSubsequentLose() > 0;
    }

    private boolean wonLastRound(final FightCommand command, final ResolvationData resolvationData) {
        return fetchBattleStatistics(command, resolvationData).getSubsequentWin() > 0;
    }

    private BattleStatistics fetchBattleStatistics(final FightCommand command, final ResolvationData resolvationData) {
        final String enemyId = getEnemyId(resolvationData);
        final BattleStatistics battleStatistics = command.getBattleStatistics(enemyId);
        return battleStatistics;
    }

    private String getEnemyId(final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        return interactionHandler.peekLastFightCommand((FfCharacter) resolvationData.getCharacter(), "enemyId");
    }

    private boolean fightCommandIssued(final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        return interactionHandler.peekLastFightCommand(character) != null;
    }

    private boolean enemyProficientFighter(final ResolvationData resolvationData) {
        return proficientEnemies.contains(getEnemyId(resolvationData));
    }

    private boolean fightWithFist(final ResolvationData resolvationData) {
        final FfItem equippedWeapon = getEquippedWeapon(resolvationData);
        final String weaponId = equippedWeapon.getId();
        return "defWpn".equals(weaponId) || "1000".equals(weaponId);
    }

    public void setProficientEnemies(final List<String> proficientEnemies) {
        this.proficientEnemies = proficientEnemies;
    }
}
