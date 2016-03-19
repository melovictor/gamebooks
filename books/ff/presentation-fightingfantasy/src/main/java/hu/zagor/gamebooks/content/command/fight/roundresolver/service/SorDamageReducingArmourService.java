package hu.zagor.gamebooks.content.command.fight.roundresolver.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Service class for handling the damage reducing armour throughout all the different fight types.
 * @author Tamas_Szekeres
 */
@Component
public class SorDamageReducingArmourService {
    private static final int ARMOUR_PROTECTION_LIMIT = 4;
    private static final String ARMOUR_ID = "3064";

    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer diceResultRenderer;

    /**
     * Sets up damage protection for the character before the actual damage handling can happen.
     * @param dto the {@link FightDataDto} object
     */
    public void setUpDamageProtection(final FightDataDto dto) {
        final FfCharacterItemHandler itemHandler = dto.getCharacterHandler().getItemHandler();
        final FfCharacter character = dto.getCharacter();
        if (itemHandler.hasEquippedItem(character, ARMOUR_ID)) {
            if (rollSuccessful(dto)) {
                setDamageProtection(dto, 1);
            } else {
                setDamageProtection(dto, 0);
            }
        } else {
            setDamageProtection(dto, 0);
        }
    }

    private boolean rollSuccessful(final FightDataDto dto) {
        boolean haveProtection = false;

        final FightCommandMessageList messages = dto.getMessages();
        final int[] randomNumber = generator.getRandomNumber(1);

        messages.addKey("page.ff.label.random.after", diceResultRenderer.render(generator.getDefaultDiceSide(), randomNumber), randomNumber[0]);
        if (randomNumber[0] > ARMOUR_PROTECTION_LIMIT) {
            haveProtection = true;
            messages.addKey("page.sor.armourProtection." + haveProtection);
        }

        return haveProtection;
    }

    private void setDamageProtection(final FightDataDto dto, final int value) {
        dto.getAttributeHandler().handleModification(dto.getCharacter(), "damageProtection", value, ModifyAttributeType.set);
    }
}
