package hu.zagor.gamebooks.lw.lw.fotw.mvc.books.newgame.service.equipment;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.handler.item.LwCharacterItemHandler;
import hu.zagor.gamebooks.lw.mvc.book.newgame.domain.LwCharGenEquipment;
import hu.zagor.gamebooks.lw.mvc.book.newgame.domain.LwCharGenInput;
import hu.zagor.gamebooks.lw.mvc.book.newgame.service.equipment.LwEquipmentMapper;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Equipment mapper for LW2.
 * @author Tamas_Szekeres
 */
public class Lw2EquipmentMapper implements LwEquipmentMapper {
    private static final int DICE_SIDE = 10;

    @Autowired @Qualifier("d10") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer diceRenderer;

    @Override
    public void mapEquipments(final LwCharacter character, final Map<String, Object> result, final LwCharacterItemHandler itemHandler, final LwCharGenInput input) {
        final DiceConfiguration d10Configuration = new DiceConfiguration(1, 0, 9);
        final int[] goldCrowns = generator.getRandomNumber(d10Configuration);
        final int crowns = 10 + goldCrowns[0];

        final List<LwCharGenEquipment> equipments = input.getEquipments();
        if (equipments != null) {
            for (final LwCharGenEquipment equipment : equipments) {
                itemHandler.addItem(character, equipment.getId(), equipment.getAmount());
            }
        }

        result.put("lwGoldCrowns", crowns + diceRenderer.render(DICE_SIDE, goldCrowns));
        result.put("lwGoldCrownsNumeric", crowns);
        character.getMoney().setGoldCrowns(crowns);
    }

}
