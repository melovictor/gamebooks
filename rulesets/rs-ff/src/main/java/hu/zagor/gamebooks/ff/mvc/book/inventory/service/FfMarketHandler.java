package hu.zagor.gamebooks.ff.mvc.book.inventory.service;

import hu.zagor.gamebooks.complex.mvc.book.inventory.service.ComplexMarketHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import org.springframework.stereotype.Component;

/**
 * {@link MarketHandler} implementation for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
@Component
public class FfMarketHandler extends ComplexMarketHandler<FfCharacter> {

    @Override
    protected int getGold(final FfCharacter character) {
        return character.getGold();
    }

}
