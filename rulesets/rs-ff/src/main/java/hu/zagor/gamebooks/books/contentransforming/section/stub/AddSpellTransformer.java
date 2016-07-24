package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import org.springframework.util.Assert;
import org.w3c.dom.Node;

/**
 * Class for transforming the "add" elements of a paragraph.
 * @author Tamas_Szekeres
 */
public class AddSpellTransformer extends AbstractStubTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        Assert.notNull(parent, "The parameter 'parent' cannot be null!");
        Assert.notNull(node, "The parameter 'node' cannot be null!");
        Assert.notNull(data, "The parameter 'data' cannot be null!");
        final SorParagraphData sorData = (SorParagraphData) data;
        final int amount = extractIntegerAttribute(node, "amount");
        sorData.addSpellModifyAttributes((ModifyAttribute) getBeanFactory().getBean("modifyAttribute", "stamina", amount, ModifyAttributeType.change));
    }
}
