package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.ParagraphData;

import java.util.List;

import org.mvel2.MVEL;

/**
 * Class for resolving an item-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckItemsCommand implements ItemCheckStubCommand {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final Character character, final CharacterHandler characterHandler) {
        ParagraphData toResolve;
        final String expression = parent.getId();
        final boolean hasItem = resolveComplexItemCheckExpression(character.getEquipment(), expression);

        if (hasItem) {
            toResolve = parent.getHave();
        } else {
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }

    private boolean resolveComplexItemCheckExpression(final List<Item> equipment, final String expression) {
        String resolvedExpression = expression;
        resolvedExpression = markPossessions(equipment, resolvedExpression);
        resolvedExpression = markUnpossessedItems(resolvedExpression);
        resolvedExpression = switchOperands(resolvedExpression);
        return (boolean) MVEL.eval(resolvedExpression);
    }

    private String markPossessions(final List<Item> equipment, final String resolvedExpression) {
        String nextStep = resolvedExpression;
        for (final Item item : equipment) {
            nextStep = nextStep.replaceAll("\\b" + item.getId() + "\\b", "true");
        }
        return nextStep;
    }

    private String switchOperands(final String resolvedExpression) {
        return resolvedExpression.replaceAll("or", "||").replaceAll("and", "&&").replaceAll("not", "!");
    }

    private String markUnpossessedItems(final String expression) {
        String resolvedExpression = expression;
        final String[] pieces = resolvedExpression.replaceAll("true", "").replaceAll("false", "").replace("not", "").replaceAll(" and ", " ").replaceAll(" or ", " ")
            .replaceAll("\\(", " ").replaceAll("\\)", " ").split(" ");
        for (final String piece : pieces) {
            if (piece.trim().length() > 0) {
                resolvedExpression = resolvedExpression.replaceAll("\\b" + piece.trim() + "\\b", "false");
            }
        }
        return resolvedExpression;
    }

}
