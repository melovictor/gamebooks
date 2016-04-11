package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.Sor4RedEyeRetaliationStrikeService;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * All at once fight round resolver for Sor4.
 * @author Tamas_Szekeres
 */
@Component("allAtOncesor4FightRoundResolver")
public class AllAtOnceSor4FightRoundResolver extends AllAtOnceSorFightRoundResolver {
    @Resource(name = "sor4Sightmasters") private Set<String> sightmasters;
    @Autowired private Sor4RedEyeRetaliationStrikeService retaliationHandler;

    @Override
    protected void damageEnemy(final FightCommand command, final FightDataDto dto) {
        super.damageEnemy(command, dto);
        final FfEnemy enemy = dto.getEnemy();
        if (enemy.getStamina() <= 0 && isSightmaster(enemy) && underSpell(dto)) {
            command.getRoundEvents().get(0).getParagraphData().setInterrupt(true);
        }
        if (retaliationHandler.needToRetaliate(dto)) {
            retaliationHandler.calculateRetaliationStrike(dto);
        }
    }

    private boolean underSpell(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final FfCharacterItemHandler itemHandler = dto.getCharacterHandler().getItemHandler();
        return itemHandler.hasItem(character, "4084");
    }

    private boolean isSightmaster(final FfEnemy enemy) {
        return sightmasters.contains(enemy.getId());
    }
}
