package hu.zagor.gamebooks.books.contentransforming;

import hu.zagor.gamebooks.books.AbstractTransformerTest;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link AbstractTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class AbstractTransformerUnitTest extends AbstractTransformerTest {

    private TestableTransformer underTest;
    private IMocksControl mockControl;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new TestableTransformer();
        node = mockControl.createMock(Node.class);
        nodeValue = mockControl.createMock(Node.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testExtractBooleanAttributeWhenAttributeExistsShouldReturnAttributeValue() {
        // GIVEN
        expectAttribute("booleanAttribute", "true");
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.extractBooleanAttribute(node, "booleanAttribute", false);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testExtractBooleanAttributeWhenAttributeDoesNotExistShouldReturnDefaultValue() {
        // GIVEN
        expectAttribute("booleanAttribute");
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.extractBooleanAttribute(node, "booleanAttribute", false);
        // THEN
        Assert.assertFalse(returned);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private class TestableTransformer extends AbstractTransformer {
    }

}
