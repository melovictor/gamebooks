package hu.zagor.gamebooks.ff.ff.sots.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.roundresolver.SingleFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import org.springframework.stereotype.Component;

/**
 * Customized single fight round resolver for FF20 that reports mutual block in case of the second round of dual sword fight hit.
 * @author Tamas_Szekeres
 */
@Component
public class SingleBaseFf20FightRoundResolver extends SingleFightRoundResolver {

    @Override
    protected void damageSelf(final FightDataDto dto) {
        final String nitoRound = dto.getCharacterHandler().getInteractionHandler().peekInteractionState(dto.getCharacter(), "nito");
        if (nitoRound == null) {
            super.damageSelf(dto);
        } else {
            dto.getMessages().addKey("page.ff.label.fight.single.failedAttack", dto.getEnemy().getCommonName());
        }
    }
}
