package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there are more than one enemies attacking us at the same time, while we have a support attacking the same enemy we do.
 * @author Tamas_Szekeres
 */
@Component
public class AllAtOnceSupportedFightRoundResolver extends AllAtOnceFightRoundResolver {
    @Autowired @Qualifier("singleSupportedFightRoundResolver") private FfFightRoundResolver singleSupportedResolver;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        FightRoundResult[] resolveRound;
        final Character character = resolvationData.getCharacter();
        if (character instanceof FfAllyCharacter) {
            resolveRound = singleSupportedResolver.resolveRound(command, resolvationData, beforeRoundResult);
        } else {
            resolveRound = super.resolveRound(command, resolvationData, beforeRoundResult);
        }
        return resolveRound;
    }

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
    protected void recordHeroAttachStrength(final FightCommandMessageList messages, final int[] selfAttackStrengthValues, final int selfAttackStrength,
        final FfCharacter character) {
        if (character instanceof FfAllyCharacter) {
            final String renderedDice = getDiceResultRenderer().render(6, selfAttackStrengthValues);
            messages.addKey("page.ff.label.fight.single.attackStrength.ally", new Object[]{renderedDice, selfAttackStrength, character.getName()});
            getLogger().debug("Attack strength for self: {}", selfAttackStrength);
        } else {
            super.recordHeroAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
        }
    }
}
