package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.newgame.service.equipment;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.mvc.book.newgame.service.equipment.InitialItemDescriptor;
import hu.zagor.gamebooks.lw.mvc.book.newgame.service.equipment.LwEquipmentMapper;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.messages.MessageSource;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Equipment mapper for LW1.
 * @author Tamas_Szekeres
 */
public class Lw1EquipmentMapper implements LwEquipmentMapper {
    private static final int DICE_SIDE = 10;

    @Autowired @Qualifier("d10") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer diceRenderer;
    private Map<Integer, InitialItemDescriptor> randomItem;
    @Autowired private MessageSource messageSource;

    @Override
    public void mapEquipments(final LwCharacter character, final Map<String, Object> result, final CharacterItemHandler itemHandler) {
        final DiceConfiguration d10Configuration = new DiceConfiguration(1, 0, 9);
        final int[] goldCrowns = generator.getRandomNumber(d10Configuration);
        final int[] equipment = generator.getRandomNumber(d10Configuration);

        result.put("lwGoldCrowns", goldCrowns[0] + diceRenderer.render(DICE_SIDE, goldCrowns));
        result.put("lwGoldCrownsNumeric", goldCrowns[0]);
        character.getMoney().setGoldCrowns(goldCrowns[0]);

        final InitialItemDescriptor descriptor = randomItem.get(equipment[0]);
        itemHandler.addItem(character, descriptor.getItemId(), descriptor.getAmount());

        result.put("lwStartingEquipment",
            messageSource.getMessage("page.lw1.characterGeneration.equipment." + descriptor.getItemId(), diceRenderer.render(DICE_SIDE, equipment)));
    }

    public void setRandomItem(final Map<Integer, InitialItemDescriptor> randomItem) {
        this.randomItem = randomItem;
    }

}
