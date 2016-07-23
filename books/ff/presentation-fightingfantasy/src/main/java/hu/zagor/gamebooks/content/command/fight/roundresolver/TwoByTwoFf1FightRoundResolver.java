package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Ff1-specific single fight round resolver.
 * @author Tamas_Szekeres
 */
@Component("twoByTwoff1FightRoundResolver")
public class TwoByTwoFf1FightRoundResolver extends TwoByTwoFightRoundResolver {

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
        super.damageSelf(dto);
        character.setDamageProtection(0);
    }

    private boolean shieldBlocksDamage(final FightDataDto dto) {
        boolean successfulBlock = false;

        final int[] result = getGenerator().getRandomNumber(1);
        successfulBlock = result[0] == SHIELD_BLOCKS;
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.ff.label.random.after", getDiceResultRenderer().render(getGenerator().getDefaultDiceSide(), result), result[0]);
        messages.addKey("page.ff1.label.shield." + successfulBlock);

        return successfulBlock;
    }
}
