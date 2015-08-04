package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;

import org.springframework.stereotype.Component;

/**
 * Ff18-specific all-at-once fight round resolver.
 * @author Tamas_Szekeres
 */
@Component("allAtOnce18FightRoundResolver")
public class AllAtOnce18FightRoundResolver extends AllAtOnceFightRoundResolver {

    @Override
    protected void damageEnemy(final FightCommand command, final FightDataDto dto) {
        super.damageEnemy(command, dto);

        final FfEnemy enemy = dto.getEnemy();
        if ("50".equals(enemy.getId()) && enemy.getStamina() <= 0) {
            command.getAfterBounding().getCommands().clear();
        }
    }
}
