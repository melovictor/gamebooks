package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ComplexParagraphData;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AddTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class AddTransformerTest extends AbstractTransformerTest {
    @UnderTest private AddTransformer underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private BeanFactory beanFactory;
    @Mock private BookParagraphDataTransformer parent;
    @Mock private ComplexParagraphData data;
    private ModifyAttribute modifyAttrib;

    @BeforeClass
    public void setUpClass() {
        modifyAttrib = new ModifyAttribute("stamina", 9, ModifyAttributeType.change);
    }

    public void testDoTransformShouldAddModifyAttributeElement() {
        // GIVEN
        expectAttribute("to", "stamina");
        expectAttribute("amount", "9");
        expectAttribute("type");
        expectAttribute("goldOnly");

        expect(beanFactory.getBean("modifyAttribute", "stamina", 9, ModifyAttributeType.change, false)).andReturn(modifyAttrib);
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

}
