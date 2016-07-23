package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.SorDamageReducingArmourService;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Sor-specific fight round resolver for fighting always two bad guys.
 * @author Tamas_Szekeres
 */
@Component("alwaysTwosor4FightRoundResolver")
public class AlwaysTwoSor4FightRoundResolver extends AlwaysTwoFightRoundResolver {
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
