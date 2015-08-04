package hu.zagor.gamebooks.books.contentransforming.section.stub.itemcheck;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;

import org.w3c.dom.Node;

/**
 * Class for transforming have sections of an item check.
 * @author Tamas_Szekeres
 */
public class ItemCheckHaveEquippedTransformer extends AbstractCommandSubTransformer<ItemCheckCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ItemCheckCommand command,
            final ChoicePositionCounter positionCounter) {
        command.setHaveEquipped(parent.parseParagraphData(positionCounter, node));
    }

}
