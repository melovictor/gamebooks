package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Interface for classes which has special logic for rolling the Attack Strength for the hero character.
 * @author Tamas_Szekeres
 */
public interface HeroAttackStrengthRoller {

    /**
     * Calculates the attack strength for the hero.
     * @param character the {@link FfCharacter} for which the attack strength has to be calculated
     * @param command the {@link FightCommand} object
     * @param attributeHandler the {@link FfAttributeHandler} object
     * @return the calculated attack strength
     */
    int[] getSelfAttackStrength(FfCharacter character, FightCommand command, FfAttributeHandler attributeHandler);

}
