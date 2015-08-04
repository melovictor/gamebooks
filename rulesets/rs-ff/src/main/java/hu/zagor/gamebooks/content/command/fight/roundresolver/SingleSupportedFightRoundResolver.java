package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there is either a single enemy, multiple enemies that must be handled as a single opponent or multiple enemies that must be fought one by
 * one.
 * @author Tamas_Szekeres
 */
@Component("singleSupportedFightRoundResolver")
public class SingleSupportedFightRoundResolver extends SingleFightRoundResolver {

    @Override
    protected void resolveTieMessage(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        if (character instanceof FfAllyCharacter) {
            final FightCommandMessageList messages = dto.getMessages();
            final FfEnemy enemy = dto.getEnemy();
            messages.addKey("page.ff.label.fight.single.tied.ally", new Object[]{enemy.getName(), character.getName()});
        } else {
            super.resolveTieMessage(dto);
        }
    }

    @Override
    protected void resolveDefeatMessage(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        if (character instanceof FfAllyCharacter) {
            final FightCommandMessageList messages = dto.getMessages();
            final FfEnemy enemy = dto.getEnemy();
            character.changeStamina(enemy.getStaminaDamage());
            messages.addKey("page.ff.label.fight.single.failedAttack.ally", new Object[]{enemy.getName(), character.getName()});
        } else {
            super.resolveDefeatMessage(dto);
        }
    }

    @Override
    protected void resolveVictoryMessage(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        if (character instanceof FfAllyCharacter) {
            final FightCommandMessageList messages = dto.getMessages();
            final FfEnemy enemy = dto.getEnemy();
            messages.addKey("page.ff.label.fight.single.successfulAttack.ally", new Object[]{enemy.getName(), character.getName()});
        } else {
            super.resolveVictoryMessage(dto);
        }
    }

    @Override
    protected void recordAttachStrength(final FightCommandMessageList messages, final int[] selfAttackStrengthValues, final int selfAttackStrength,
        final FfCharacter character) {
        if (character instanceof FfAllyCharacter) {
            messages.addKey("page.ff.label.fight.single.attackStrength.ally", new Object[]{selfAttackStrengthValues[1], selfAttackStrengthValues[2], selfAttackStrength,
                character.getName()});
            getLogger().debug("Attack strength for self: {}", selfAttackStrength);
        } else {
            super.recordAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
        }
    }

}
