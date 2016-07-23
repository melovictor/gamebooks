package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.Ff7SpecialAttackStrengthGenerator;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Ff7-specific all-at-once fight round resolver.
 * @author Tamas_Szekeres
 */
@Component("allAtOnceff7FightRoundResolver")
public class AllAtOnceFf7FightRoundResolver extends AllAtOnceFightRoundResolver {

    @Autowired
    private Ff7SpecialAttackStrengthGenerator attackStrengthGenerator;

    @Override
    int[] getSelfAttackStrength(final FfCharacter character, final FfFightCommand command, final FfAttributeHandler attributeHandler) {
        int[] selfAttackStrength = attackStrengthGenerator.getSelfAttackStrength(character, command, attributeHandler);

        if (selfAttackStrength == null) {
            selfAttackStrength = super.getSelfAttackStrength(character, command, attributeHandler);
        }

        return selfAttackStrength;
    }

    @Override
    public int[] getEnemyAttackStrength(final FfEnemy enemy, final FfFightCommand command) {
        int[] enemyAttackStrength = attackStrengthGenerator.getEnemyAttackStrength(enemy, command);

        if (enemyAttackStrength == null) {
            enemyAttackStrength = super.getEnemyAttackStrength(enemy, command);
        }

        return enemyAttackStrength;
    }
}
