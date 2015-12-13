package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there is either a single enemy, multiple enemies that must be handled as a single opponent or multiple enemies that must be fought one by
 * one.
 * @author Tamas_Szekeres
 */
@Component("singleAllyFightRoundResolver")
public class SingleAllyFightRoundResolver extends SingleFightRoundResolver {

    @Override
    protected void resolveTieMessage(final FightDataDto dto) {
        final FightCommandMessageList messages = dto.getMessages();
        final FfEnemy enemy = dto.getEnemy();
        final FfCharacter character = dto.getCharacter();
        messages.addKey("page.ff.label.fight.single.tied.ally", new Object[]{enemy.getName(), getName(character)});
    }

    private String getName(final FfCharacter character) {
        return ((FfAllyCharacter) character).getName();
    }

    @Override
    protected void resolveDefeatMessage(final FightDataDto dto) {
        final FightCommandMessageList messages = dto.getMessages();
        final FfEnemy enemy = dto.getEnemy();
        final FfCharacter character = dto.getCharacter();
        messages.addKey("page.ff.label.fight.single.failedDefense.ally", new Object[]{enemy.getName(), getName(character)});
    }

    @Override
    protected void resolveVictoryMessage(final FightDataDto dto) {
        final FightCommandMessageList messages = dto.getMessages();
        final FfEnemy enemy = dto.getEnemy();
        final FfCharacter character = dto.getCharacter();
        messages.addKey("page.ff.label.fight.single.successfulAttack.ally", new Object[]{enemy.getName(), getName(character)});
    }

    @Override
    protected void recordAttachStrength(final FightCommandMessageList messages, final int[] selfAttackStrengthValues, final int selfAttackStrength,
        final FfCharacter character) {
        final String renderedDice = getDiceResultRenderer().render(6, selfAttackStrengthValues);
        messages.addKey("page.ff.label.fight.single.attackStrength.ally", new Object[]{renderedDice, selfAttackStrength, getName(character)});
        getLogger().debug("Attack strength for self: {}", selfAttackStrength);
    }

    @Override
    void doLoseFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        final FfAllyCharacter firstAlly = command.getFirstAlly();
        final FfCharacter character = dto.getCharacter();

        if (firstAlly == character) {
            super.doLoseFight(command, result, enemyIdx, dto);
        } else {
            dto.getMessages().addKey("page.ff.label.fight.single.failedAttack.ally", new Object[]{dto.getEnemy().getName(), character.getName()});
        }
    }

}
