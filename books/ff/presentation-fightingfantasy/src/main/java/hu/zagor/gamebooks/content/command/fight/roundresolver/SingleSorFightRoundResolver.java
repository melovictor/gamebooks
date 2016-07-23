package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.SorDamageReducingArmourService;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Abstract extension of the {@link SingleFightRoundResolver} class for resolving single fight rounds in SOR.
 * @author Tamas_Szekeres
 */
public abstract class SingleSorFightRoundResolver extends SingleFightRoundResolver {

    @Autowired private SorDamageReducingArmourService damageReducingArmourService;
    @Autowired @Qualifier("sorHeroAttackStrengthRoller") private HeroAttackStrengthRoller heroAttackStrengthRoller;

    @Override
    protected void damageSelf(final FightDataDto dto) {
        damageReducingArmourService.setUpDamageProtection(dto);
        super.damageSelf(dto);
    }

    @Override
    int[] getSelfAttackStrength(final FfCharacter character, final FfFightCommand command, final FfAttributeHandler attributeHandler) {
        return heroAttackStrengthRoller.getSelfAttackStrength(character, command, attributeHandler);
    }

}
