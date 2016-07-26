package hu.zagor.gamebooks.content.command.market;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Resolver class for "market" elements for FF.
 * @author Tamas_Szekeres
 */
public class FfMarketCommandResolver extends ComplexMarketCommandResolver {

    @Override
    protected int getDefaultGoldAmount(final Character character) {
        return ((FfCharacter) character).getGold();
    }

}
