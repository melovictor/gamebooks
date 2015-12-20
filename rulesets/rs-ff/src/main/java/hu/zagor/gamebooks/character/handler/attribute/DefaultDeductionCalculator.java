package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link DeductionCalculator} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultDeductionCalculator implements DeductionCalculator {

    @Override
    public GoldItemDeduction calculateDeductibleElements(final FfCharacter character, final int amount) {
        Assert.isTrue(amount > 0, "The amount to calculate must be bigger than zero.");
        final List<FfItem> valuables = gatherValuables(character);
        return calculateAmount(valuables, character.getGold(), amount);
    }

    private List<FfItem> gatherValuables(final FfCharacter character) {
        final List<FfItem> valuables = new ArrayList<>();
        for (final FfItem item : character.getFfEquipment()) {
            if (item.getItemType() == ItemType.valuable) {
                valuables.add(item);
            }
        }
        Collections.sort(valuables, new Comparator<FfItem>() {

            @Override
            public int compare(final FfItem o1, final FfItem o2) {
                return o2.getGold() - o1.getGold();
            }

        });
        return valuables;
    }

    private GoldItemDeduction calculateAmount(final List<FfItem> valuables, final int gold, final int amount) {
        GoldItemDeduction deduction = calculateExactMatch(valuables, gold, amount);
        if (deduction == null) {
            deduction = calculateAmount(valuables, gold, amount + 1);
        }
        return deduction;
    }

    private GoldItemDeduction calculateExactMatch(final List<FfItem> valuables, final int gold, final int amount) {
        int amountLeft = amount;
        GoldItemDeduction deduction = new GoldItemDeduction();
        for (final FfItem item : valuables) {
            if (item.getGold() <= amountLeft) {
                amountLeft -= item.getGold();
                deduction.getItems().add(item);
            }
        }
        if (amountLeft <= gold) {
            deduction.setGold(amountLeft);
        } else {
            if (valuables.isEmpty()) {
                deduction = null;
            } else {
                deduction = calculateExactMatch(valuables.subList(1, valuables.size()), gold, amount);
            }
        }

        return deduction;
    }

}
