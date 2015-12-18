package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Interface for an object that is capable of selecting specific items and numbers of gold pieces that add up to a specific value (if possible) or the closest one to that from
 * above (if not).
 * @author Tamas_Szekeres
 */
public interface DeductionCalculator {

    /**
     * Calculate the elements to be deducted from the {@link FfCharacter} to add up to the specified amount.
     * @param character the {@link FfCharacter} whose equipment needs to be considered
     * @param amount the amount to be reached
     * @return the bean storing the deduction information
     */
    GoldItemDeduction calculateDeductibleElements(FfCharacter character, int amount);

}
