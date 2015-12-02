package hu.zagor.gamebooks.books;

import static org.easymock.EasyMock.expect;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Abstract class that is capable of setting up generic mocks for transformers.
 * @author Tamas_Szekeres
 */
public abstract class AbstractTransformerTest {

    @Mock public Node node;
    @Mock public NamedNodeMap nodeMap;
    @Mock public Node nodeValue;
    @Mock public NodeList nodeList;

    public void init(final IMocksControl mockControl) {
        node = mockControl.createMock(Node.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);
        nodeList = mockControl.createMock(NodeList.class);
    }

    /**
     * Expects a read request for an attribute which exists and has a value.
     * @param attributeName the name of the attribute to find
     * @param attributeValue the value of the attribute
     */
    public void expectAttribute(final String attributeName, final String attributeValue) {
        expect(node.getAttributes()).andReturn(nodeMap);
        expect(nodeMap.getNamedItem(attributeName)).andReturn(nodeValue);
        expect(nodeValue.getNodeValue()).andReturn(attributeValue);
    }

    /**
     * Expects a read request for an attribute that doesn't exists.
     * @param attributeName the name of the attribute
     */
    public void expectAttribute(final String attributeName) {
        expect(node.getAttributes()).andReturn(nodeMap);
        expect(nodeMap.getNamedItem(attributeName)).andReturn(null);
    }

    /**
     * Expects a read request for the textual content of the current node.
     * @param content the content
     */
    public void expectContent(final String content) {
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeValue()).andReturn(content);
    }

    /**
     * Expects a read request for the textual content of the current empty node.
     */
    public void expectContent() {
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.item(0)).andReturn(null);
    }
}
