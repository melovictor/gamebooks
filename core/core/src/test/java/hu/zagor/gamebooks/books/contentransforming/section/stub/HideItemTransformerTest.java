package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link UnhideItemTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class HideItemTransformerTest extends AbstractTransformerTest {

    private HideItemTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private ParagraphData data;
    private BeanFactory beanFactory;
    private GatheredLostItem glItem;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new HideItemTransformer();
        init(mockControl);
        data = new ParagraphData();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        underTest.setBeanFactory(beanFactory);
        glItem = new GatheredLostItem("3001", 1, 0, false);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoTransformShouldParseItemAndPutItInUnhiddenItemList() {
        // GIVEN
        expectAttribute("id", "3001");
        expectAttribute("amount");
        expectAttribute("dose");
        expectAttribute("unequippedOnly");
        expect(beanFactory.getBean("gatheredLostItem", "3001", 1, 0, false)).andReturn(glItem);
        data.addHiddenItem(glItem);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
