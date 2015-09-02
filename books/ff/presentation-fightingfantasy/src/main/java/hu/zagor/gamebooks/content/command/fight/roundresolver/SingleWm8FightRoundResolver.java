package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Ff7-specific single fight round resolver.
 * @author Tamas_Szekeres
 */
@Component("singlewm8FightRoundResolver")
public class SingleWm8FightRoundResolver extends SingleFightRoundResolver {

    private static final int TORNAKK_STONE_SKIL = 3;

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer renderer;

    @Override
    protected void damageEnemy(final FightCommand command, final FightDataDto dto) {
        if ("2".equals(dto.getEnemy().getId())) {
            final int[] randomNumber = generator.getRandomNumber(1);
            final FightCommandMessageList messages = command.getMessages();
            String result;
            if (randomNumber[0] > TORNAKK_STONE_SKIL) {
                result = "defense";
            } else {
                super.damageEnemy(command, dto);
                result = "damage";
            }
            messages.addKey("page.wm8.label.fight.tornakk." + result, renderer.render(generator.getDefaultDiceSide(), randomNumber));
        } else {
            super.damageEnemy(command, dto);
        }
    }

}
