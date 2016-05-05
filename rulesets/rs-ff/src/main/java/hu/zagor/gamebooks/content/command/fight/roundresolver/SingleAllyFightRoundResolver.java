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
        final FfEnemy ally = getAlly(dto);
        messages.addKey("page.ff.label.fight.single.tied.ally" + (ally.isIndifferentAlly() ? ".indifferent" : ""), new Object[]{enemy.getName(), ally.getName()});
    }

    private FfEnemy getAlly(final FightDataDto dto) {
        final FfAllyCharacter ally = (FfAllyCharacter) dto.getCharacter();
        return ally.getAlly();
    }

    private FfEnemy getAlly(final FfCharacter character) {
        final FfAllyCharacter ally = (FfAllyCharacter) character;
        return ally.getAlly();
    }

    @Override
    protected void resolveDefeatMessage(final FightDataDto dto) {
        final FightCommandMessageList messages = dto.getMessages();
        final FfEnemy enemy = dto.getEnemy();
        final FfEnemy ally = getAlly(dto);
        messages.addKey("page.ff.label.fight.single.failedDefense.ally" + (ally.isIndifferentAlly() ? ".indifferent" : ""),
            new Object[]{enemy.getName(), ally.getName()});
    }

    @Override
    protected void resolveVictoryMessage(final FightDataDto dto) {
        final FightCommandMessageList messages = dto.getMessages();
        final FfEnemy enemy = dto.getEnemy();
        final FfEnemy ally = getAlly(dto);
        messages.addKey("page.ff.label.fight.single.successfulAttack.ally" + (ally.isIndifferentAlly() ? ".indifferent" : ""),
            new Object[]{enemy.getName(), ally.getName()});
    }

    @Override
    protected void recordHeroAttachStrength(final FightCommandMessageList messages, final int[] selfAttackStrengthValues, final int selfAttackStrength,
        final FfCharacter character) {
        final String renderedDice = getDiceResultRenderer().render(6, selfAttackStrengthValues);
        final FfEnemy ally = getAlly(character);
        messages.addKey("page.ff.label.fight.single.attackStrength.ally" + (ally.isIndifferentAlly() ? ".indifferent" : ""),
            new Object[]{renderedDice, selfAttackStrength, ally.getName()});
        getLogger().debug("Attack strength for ally: {}", selfAttackStrength);
    }

    @Override
    protected void recordEnemyAttachStrength(final FightDataDto dto, final int[] enemyAttackStrengthValues, final int enemyAttackStrength) {
        final FightCommandMessageList messages = dto.getMessages();
        final FfEnemy enemy = dto.getEnemy();

        final String renderedDice = getDiceResultRenderer().render(6, enemyAttackStrengthValues);
        final FfEnemy ally = getAlly(dto);
        messages.addKey("page.ff.label.fight.single.attackStrength.enemy" + (ally.isIndifferentAlly() ? ".indifferent" : ""),
            new Object[]{enemy.getName(), renderedDice, enemyAttackStrength});
        getLogger().debug("Attack strength for {}: {}", enemy.getName(), enemyAttackStrength);
    }

    @Override
    void doLoseFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        final FfAllyCharacter firstAlly = command.getFirstAlly();
        final FfCharacter character = dto.getCharacter();

        if (firstAlly == character) {
            super.doLoseFight(command, result, enemyIdx, dto);
        } else {
            final FfEnemy ally = getAlly(dto);
            dto.getMessages().addKey("page.ff.label.fight.single.failedAttack.ally" + (ally.isIndifferentAlly() ? ".indifferent" : ""),
                new Object[]{dto.getEnemy().getName(), ally.getName()});
        }
    }

    @Override
    int getHeroRolledDices(final FfCharacter characterObject, final FightCommand command) {
        final FfAllyCharacter character = (FfAllyCharacter) characterObject;
        return character.getAttackStrengthDices();
    }

    @Override
    int getHeroUsedDices(final FfCharacter characterObject, final FightCommand command) {
        final FfAllyCharacter character = (FfAllyCharacter) characterObject;
        return character.getAttackStrengthDices();
    }

}
