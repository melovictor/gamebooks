package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class that handles the rolling result for all of the Sorcery series.
 * @author Tamas_Szekeres
 */
@Component("sorHeroAttackStrengthRoller")
public class SorHeroAttackStrengthRoller implements HeroAttackStrengthRoller {
    private static final int LEATHER_GLOVE_STAMINA_LIMIT = 5;
    @Resource(name = "specialGloveRerollChanceTable") private Map<Integer, Integer> rerollChance;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generatorD6;
    @Autowired @Qualifier("d10") private RandomNumberGenerator generatorD10;

    @Override
    public int[] getSelfAttackStrength(final FfCharacter character, final FightCommand command, final FfAttributeHandler attributeHandler) {
        final int[] generatedAttackStrength = generatorD6.getRandomNumber(2);
        if (hasSpecialGloves(character) && hasCriticalStamina(character, attributeHandler) && wantToRerollDice(generatedAttackStrength)) {
            rerollLowestDice(generatedAttackStrength);
        }
        generatedAttackStrength[0] += attributeHandler.resolveValue(character, "attackStrength");
        return generatedAttackStrength;
    }

    private boolean hasCriticalStamina(final FfCharacter character, final FfAttributeHandler attributeHandler) {
        return attributeHandler.resolveValue(character, "stamina") < LEATHER_GLOVE_STAMINA_LIMIT;
    }

    private void rerollLowestDice(final int[] generatedAttackStrength) {
        final int lowestDice = getLowestValue(generatedAttackStrength);
        final int newDice = generatorD6.getRandomNumber(1)[0];
        generatedAttackStrength[0] += newDice - lowestDice;
        generatedAttackStrength[getIndexOf(generatedAttackStrength, lowestDice)] = newDice;
    }

    private int getIndexOf(final int[] array, final int value) {
        int index = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] == value) {
                index = i;
            }
        }
        return index;
    }

    private boolean wantToRerollDice(final int[] generatedAttackStrength) {
        final int lowestDice = getLowestValue(generatedAttackStrength);
        return rerollChance.get(lowestDice) > generatorD10.getRandomNumber(1)[0];
    }

    private int getLowestValue(final int[] generatedAttackStrength) {
        int lowestDice = Integer.MAX_VALUE;

        for (int i = 1; i < generatedAttackStrength.length; i++) {
            if (lowestDice > generatedAttackStrength[i]) {
                lowestDice = generatedAttackStrength[i];
            }
        }
        return lowestDice;
    }

    private boolean hasSpecialGloves(final FfCharacter character) {
        boolean hasGloves = false;

        for (final Item item : character.getEquipment()) {
            hasGloves |= "3043".equals(item.getId()) && item.getEquipInfo().isEquipped();
        }

        return hasGloves;
    }

}
