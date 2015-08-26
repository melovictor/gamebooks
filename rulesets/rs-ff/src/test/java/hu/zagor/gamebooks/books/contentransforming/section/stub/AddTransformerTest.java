package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link AddTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class AddTransformerTest extends AbstractTransformerTest {

    private AddTransformer underTest;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private BookParagraphDataTransformer parent;
    private FfParagraphData data;
    private ModifyAttribute modifyAttrib;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        beanFactory = mockControl.createMock(BeanFactory.class);
        node = mockControl.createMock(Node.class);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        data = mockControl.createMock(FfParagraphData.class);
        modifyAttrib = new ModifyAttribute("stamina", 9, ModifyAttributeType.change);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);

        underTest = new AddTransformer();
        underTest.setBeanFactory(beanFactory);
    }

    public void testDoTransformShouldAddModifyAttributeElement() {
        // GIVEN
        expectAttribute("to", "stamina");
        expectAttribute("amount", "9");
        expectAttribute("type");

        expect(beanFactory.getBean("ffModifyAttribute", "stamina", 9, ModifyAttributeType.change)).andReturn(modifyAttrib);
        data.addModifyAttributes(modifyAttrib);

        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testDoTransformWhenToIsMissingShouldThrowException() {
        // GIVEN
        expectAttribute("to");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN throws exception
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
