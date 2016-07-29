package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.Money;
import java.lang.reflect.InvocationTargetException;
import org.easymock.internal.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * Item check that checks whether the hero has enough money from the specified amount.
 * @author Tamas_Szekeres
 */
public class ItemCheckMoneyCommandResolver implements ItemCheckStubCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        final String moneyType = parent.getId();
        final int moneyAmount = parent.getAmount();

        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        final Money money = character.getMoney();
        final int haveAmount = getAmount(money, moneyType);

        return haveAmount >= moneyAmount ? parent.getHave() : parent.getDontHave();
    }

    private int getAmount(final Money money, final String moneyType) {
        try {
            return (int) ReflectionUtils.findMethod(Money.class, "get" + StringUtils.capitalize(moneyType)).invoke(money);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IllegalArgumentException("Cannot resolve money type " + moneyType, ex);
        }
    }

}
