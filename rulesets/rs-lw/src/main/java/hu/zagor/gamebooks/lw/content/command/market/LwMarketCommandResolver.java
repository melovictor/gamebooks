package hu.zagor.gamebooks.lw.content.command.market;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.content.command.market.ComplexMarketCommandResolver;
import hu.zagor.gamebooks.lw.character.LwCharacter;

/**
 * Resolver class for "market" elements for LW.
 * @author Tamas_Szekeres
 */
public class LwMarketCommandResolver extends ComplexMarketCommandResolver {

    @Override
    protected int getDefaultGoldAmount(final Character character) {
        return ((LwCharacter) character).getMoney().getGoldCrowns();
    }

}
