package hu.zagor.gamebooks.books.contentransforming;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Abstract class that contains common methods to use for transforming a {@link Document} object into our
 * business domain objects.
 * @author Tamas_Szekeres
 */
public abstract class AbstractTransformer implements BeanFactoryAware {

    @Resource(name = "irrelevantNodeNames")
    private List<String> irrelevantNodeNames;
    private BeanFactory beanFactory;

    /**
     * Extracts an attribute from the {@link Node}.
     * @param node the {@link Node} bean from which we want to extract the attribute, cannot be null
     * @param attributeName the name of the attribute, cannot be null
     * @return the value of the attribute if it exists or null if it doesn't
     */
    protected String extractAttribute(final Node node, final String attributeName) {
        return extractAttribute(node, attributeName, null);
    }

    /**
     * Extracts an integer attribute from the {@link Node}.
     * @param node the {@link Node} bean from which we want to extract the attribute, cannot be null
     * @param attributeName the name of the attribute, cannot be null
     * @return the integer value of the attribute if it exists or null if it doesn't
     */
    protected Integer extractIntegerAttribute(final Node node, final String attributeName) {
        final String attribute = extractAttribute(node, attributeName, null);
        return attribute == null ? null : Integer.valueOf(attribute);
    }

    /**
     * Extracts an integer attribute from the {@link Node}.
     * @param node the {@link Node} bean from which we want to extract the attribute, cannot be null
     * @param attributeName the name of the attribute, cannot be null
     * @param defaultValue the default value to return if the given attribute isn't present
     * @return the integer value of the attribute if it exists or the default value if it doesn't
     */
    protected Integer extractIntegerAttribute(final Node node, final String attributeName, final Integer defaultValue) {
        final String attribute = extractAttribute(node, attributeName, null);
        return attribute == null ? defaultValue : Integer.valueOf(attribute);
    }

    /**
     * Extracts a boolean attribute from the {@link Node}.
     * @param node the {@link Node} bean from which we want to extract the attribute, cannot be null
     * @param attributeName the name of the attribute, cannot be null
     * @param defaultValue the default value to return if the given attribute isn't present
     * @return the boolean value of the attribute if it exists or the default value if it doesn't
     */
    protected boolean extractBooleanAttribute(final Node node, final String attributeName, final boolean defaultValue) {
        final String attribute = extractAttribute(node, attributeName, null);
        return attribute == null ? defaultValue : Boolean.valueOf(attribute);
    }

    /**
     * Extracts an attribute from the {@link Node}.
     * @param node the {@link Node} bean from which we want to extract the attribute, cannot be null
     * @param attributeName the name of the attribute, cannot be null
     * @param defaultValue the default value to return if the given attribute isn't present
     * @return the value of the attribute if it exists or the default value if it doesn't
     */
    protected String extractAttribute(final Node node, final String attributeName, final String defaultValue) {
        Assert.notNull(node, "The parameter 'node' cannot be null!");
        Assert.notNull(attributeName, "The parameter 'attributeName' cannot be null!");
        final Node attributeNode = node.getAttributes().getNamedItem(attributeName);
        return attributeNode == null ? defaultValue : attributeNode.getNodeValue();
    }

    /**
     * Checks if the given node is an irrelevant node or not. A node is deemed irrelevant if it contains no
     * information whatsoever that has any effect on the game mechanics. Such irrelevant nodes are, for
     * example, the comment and empty nodes.
     * @param node the node, cannot be null
     * @return true, if the node is irrelevant, false otherwise
     */
    protected boolean irrelevantNode(final Node node) {
        Assert.notNull(node, "Parameter 'node' can not be null!");
        return irrelevantNodeNames.contains(node.getNodeName());
    }

    /**
     * Extracts textual values from a given node.
     * @param node the node to extract text from
     * @return null if the node itself was null, the text contained by the node otherwise
     */
    protected String getNodeText(final Node node) {
        final Node childNode = node.getChildNodes().item(0);
        return childNode == null ? null : childNode.getNodeValue();
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
