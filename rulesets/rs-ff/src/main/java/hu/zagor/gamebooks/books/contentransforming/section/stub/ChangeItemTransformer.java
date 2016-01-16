package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.changeitem.ChangeItemCommand;
import org.w3c.dom.Node;

/**
 * Transforms the "changeItem" element of a paragraph.
 * @author Tamas_Szekeres
 */
public class ChangeItemTransformer extends AbstractStubTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final FfParagraphData ffData = (FfParagraphData) data;

        final String id = extractAttribute(node, "id");
        final String attribute = extractAttribute(node, "attribute");
        final Integer set = extractIntegerAttribute(node, "set");
        final String change = extractAttribute(node, "change");

        final ChangeItemCommand changeItemCommand = new ChangeItemCommand();
        changeItemCommand.setAttribute(attribute);
        changeItemCommand.setChangeValue(change);
        changeItemCommand.setId(id);
        changeItemCommand.setNewValue(set);

        ffData.addItemModifyAttributes(changeItemCommand);
    }

}
