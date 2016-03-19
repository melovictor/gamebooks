package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.SorDamageReducingArmourService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of the {@link FightRoundResult} interface for resolving single fight rounds in SOR.
 * @author Tamas_Szekeres
 */
public abstract class SingleSorFightRoundResolver extends SingleFightRoundResolver {

    @Autowired private SorDamageReducingArmourService damageReducingArmourService;

    @Override
    protected void damageSelf(final FightDataDto dto) {
        damageReducingArmourService.setUpDamageProtection(dto);
        super.damageSelf(dto);
    }

}
