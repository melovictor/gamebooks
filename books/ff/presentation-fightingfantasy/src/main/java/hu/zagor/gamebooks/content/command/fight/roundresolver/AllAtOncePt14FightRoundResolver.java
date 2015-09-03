package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Ff7-specific all-at-once fight round resolver.
 * @author Tamas_Szekeres
 */
@Component("allAtOncept14FightRoundResolver")
public class AllAtOncePt14FightRoundResolver extends AllAtOnceFightRoundResolver {

    @Override
    protected void damageEnemy(final FightCommand command, final FightDataDto dto) {
        super.damageEnemy(command, dto);
        if (dto.getEnemy().getStamina() <= 0) {
            weakenHeruka(command.getResolvedEnemies());
        }
    }

    private void weakenHeruka(final List<FfEnemy> enemies) {
        for (final FfEnemy enemy : enemies) {
            enemy.setSkill(enemy.getSkill() - 1);
        }
    }

}
