package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Ff7-specific single fight round resolver.
 * @author Tamas_Szekeres
 */
@Component("singleff7FightRoundResolver")
public class SingleFf7FightRoundResolver extends SingleFightRoundResolver {

    private static final String MARKER_ENEMY_ID = "999";
    private static final int[] MAX_ROLL = new int[]{12, 6, 6};
    private static final int[] MIN_ROLL = new int[]{2, 1, 1};

    @Override
    int[] getSelfAttackStrength(final FfCharacter character, final FightCommand command, final FfAttributeHandler attributeHandler) {
        int[] selfAttackStrength;

        if (command.getRoundNumber() == 1 && hasSogHelmet(character.getEquipment())) {
            selfAttackStrength = MAX_ROLL;
            final RoundEvent e = new RoundEvent();
            e.setEnemyId(MARKER_ENEMY_ID);
            command.getRoundEvents().add(e);
        } else {
            if (command.getRoundNumber() == 1 && "41".equals(command.getEnemies().get(0))) {
                selfAttackStrength = MIN_ROLL;
            } else {
                selfAttackStrength = super.getSelfAttackStrength(character, command, attributeHandler);
            }
        }

        return selfAttackStrength;
    }

    private boolean hasSogHelmet(final List<Item> equipment) {
        boolean hasHelmet = false;

        for (final Item item : equipment) {
            hasHelmet |= "3009".equals(item.getId()) && item.getEquipInfo().isEquipped();
        }

        return hasHelmet;
    }

    @Override
    public int[] getEnemyAttackStrength(final FfEnemy enemy, final FightCommand command) {
        int[] enemyAttackStrength;

        if (command.getRoundNumber() == 1 && !command.getRoundEvents().isEmpty() && MARKER_ENEMY_ID.equals(command.getRoundEvents().get(0).getEnemyId())) {
            enemyAttackStrength = MIN_ROLL;
        } else {
            if (command.getRoundNumber() == 1 && "41".equals(command.getEnemies().get(0))) {
                enemyAttackStrength = MAX_ROLL;
            } else {
                enemyAttackStrength = super.getEnemyAttackStrength(enemy, command);
            }
        }

        return enemyAttackStrength;
    }
}
