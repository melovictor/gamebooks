package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.content.gathering.GatheredLostItem;

import org.w3c.dom.Node;

/**
 * Class for transforming gather/lose item sections of a paragraph.
 * @author Tamas_Szekeres
 */
public abstract class GatherLoseItemTransformer extends AbstractStubTransformer {

    /**
     * Method that actually does the data collecting so the specific transformers doesn't have to.
     * @param node the data node
     * @return the created {@link GatheredLostItem} bean
     */
    protected GatheredLostItem parseGatherLoseItem(final Node node) {
        final String id = extractAttribute(node, "id");
        final int amount = extractIntegerAttribute(node, "amount", 1);
        final int dose = extractIntegerAttribute(node, "dose", 0);
        final boolean unequippedOnly = extractBooleanAttribute(node, "unequippedOnly", false);
        return (GatheredLostItem) getBeanFactory().getBean("gatheredLostItem", id, amount, dose, unequippedOnly);
    }

}
