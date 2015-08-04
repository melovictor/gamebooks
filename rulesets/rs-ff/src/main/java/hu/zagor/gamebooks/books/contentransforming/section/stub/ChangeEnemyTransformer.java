package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.changeenemy.ChangeEnemyCommand;

import org.w3c.dom.Node;

/**
 * Transforms the "changeEnemy" element of a paragraph.
 * @author Tamas_Szekeres
 */
public class ChangeEnemyTransformer extends AbstractStubTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final FfParagraphData ffData = (FfParagraphData) data;

        final String id = extractAttribute(node, "id");
        final String attribute = extractAttribute(node, "attribute");
        final Integer set = extractIntegerAttribute(node, "set");
        final Integer change = extractIntegerAttribute(node, "change");

        final ChangeEnemyCommand changeEnemyCommand = new ChangeEnemyCommand();
        changeEnemyCommand.setAttribute(attribute);
        changeEnemyCommand.setChangeValue(change);
        changeEnemyCommand.setId(id);
        changeEnemyCommand.setNewValue(set);

        ffData.addEnemyModifyAttributes(changeEnemyCommand);
    }

}
