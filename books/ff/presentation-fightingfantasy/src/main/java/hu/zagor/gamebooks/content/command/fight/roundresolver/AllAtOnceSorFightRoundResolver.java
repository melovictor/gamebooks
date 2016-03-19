package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.SorDamageReducingArmourService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All at once fight round resolver for Sor.
 * @author Tamas_Szekeres
 */
public class AllAtOnceSorFightRoundResolver extends AllAtOnceFightRoundResolver {
    @Autowired private SorDamageReducingArmourService damageReducingArmourService;

    @Override
    protected void damageSelf(final FightDataDto dto) {
        damageReducingArmourService.setUpDamageProtection(dto);
        super.damageSelf(dto);
    }
}
