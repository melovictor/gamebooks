package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import java.util.List;
import java.util.Map;

/**
 * FF6-specific extension of {@link FightCommandBasicSubResolver}.
 * @author Tamas_Szekeres
 */
public class Ff6FightCommandBasicSubResolver extends FightCommandBasicSubResolver {

    private static final int SCORPION_OVERKILL_AS = 22;
    private static final String SCORPION_A = "16a";
    private static final String SCORPION_B = "16b";

    private static final String GUARD_DOG_A = "22";
    private static final String GUARD_DOG_B = "23";

    @Override
    public List<ParagraphData> doResolve(final FfFightCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> resolveList = super.doResolve(command, resolvationData);

        handleScorpionBattleInterruption(command, resolveList);
        handleGuardDogFleeOption(command, resolvationData);

        return resolveList;
    }

    private void handleGuardDogFleeOption(final FfFightCommand command, final ResolvationData resolvationData) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        final FfEnemy dogA = (FfEnemy) enemies.get(GUARD_DOG_A);
        final FfEnemy dogB = (FfEnemy) enemies.get(GUARD_DOG_B);
        if (dogA.getStamina() <= 0 || dogB.getStamina() <= 0) {
            FightFleeData fleeData = command.getFleeData();
            if (fleeData == null) {
                fleeData = new FightFleeData();
                fleeData.setAfterRound(0);
                command.setFleeData(fleeData);
                command.setFleeAllowed(true);
            }
        }
    }

    private void handleScorpionBattleInterruption(final FfFightCommand command, final List<ParagraphData> resolveList) {
        final int attackStrengthA = nz(command.getAttackStrengths().get(SCORPION_A));
        final int attackStrengthB = nz(command.getAttackStrengths().get(SCORPION_B));
        if (attackStrengthA >= SCORPION_OVERKILL_AS || attackStrengthB >= SCORPION_OVERKILL_AS) {
            resolveList.clear();
            resolveList.add(command.getFlee());
            command.setOngoing(false);
        }
    }

    private int nz(final Integer value) {
        return value == null ? 0 : value;
    }
}
