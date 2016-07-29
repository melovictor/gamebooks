package hu.zagor.gamebooks.ff.mvc.book.inventory.service;

import hu.zagor.gamebooks.complex.mvc.book.inventory.service.ComplexMarketHandler;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import org.springframework.stereotype.Component;

/**
 * {@link MarketHandler} implementation for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
@Component
public class LwMarketHandler extends ComplexMarketHandler<LwCharacter> {

    @Override
    protected int getGold(final LwCharacter character) {
        return character.getMoney().getGoldCrowns();
    }

}
