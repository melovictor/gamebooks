package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Ff1-specific single fight round resolver.
 * @author Tamas_Szekeres
 */
@Component("singleff1FightRoundResolver")
public class SingleFf1FightRoundResolver extends SingleFightRoundResolver {

    private static final int DAMAGE_AVOIDED = 6;
    private static final int SHIELD_BLOCKS = 6;

    @Override
    protected void damageSelf(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        character.setDamageProtection(0);
        final FfCharacterItemHandler itemHandler = dto.getCharacterHandler().getItemHandler();
        if (itemHandler.hasEquippedItem(character, "3005")) {
            if (shieldBlocksDamage(dto)) {
                character.setDamageProtection(1);
            }
        }

        if ("58".equals(dto.getEnemy().getId())) {
            recalculateWarlocksDamage(dto);
        }

        super.damageSelf(dto);
        character.setDamageProtection(0);
    }

    private void recalculateWarlocksDamage(final FightDataDto dto) {
        final FfEnemy enemy = dto.getEnemy();
        enemy.setStaminaDamage(2);

        final int[] throwResult = getGenerator().getRandomNumber(1);
        if (throwResult[0] == DAMAGE_AVOIDED) {
            enemy.setStaminaDamage(0);
        } else if (throwResult[0] % 2 == 0) {
            enemy.setStaminaDamage(1);
        }
        dto.getMessages().addKey("page.ff.label.random.after", getDiceResultRenderer().render(getGenerator().getDefaultDiceSide(), throwResult), throwResult[0]);
    }

    private boolean shieldBlocksDamage(final FightDataDto dto) {
        boolean successfulBlock = false;

        final int[] result = getGenerator().getRandomNumber(1);
        successfulBlock = result[0] == SHIELD_BLOCKS;
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.ff.label.random.after", getDiceResultRenderer().render(getGenerator().getDefaultDiceSide(), result), result[0]);
        messages.addKey("page.ff1.label.shield." + successfulBlock, dto.getEnemy().getName());

        return successfulBlock;
    }
}
