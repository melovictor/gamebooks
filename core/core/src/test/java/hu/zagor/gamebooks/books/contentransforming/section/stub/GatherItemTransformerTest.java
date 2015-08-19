package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link GatherItemTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class GatherItemTransformerTest extends AbstractTransformerTest {

    private static final String ID = "3001";
    private static final String AMOUNT_STRING = "3";
    private static final int AMOUNT = 3;
    private GatherItemTransformer underTest;
    private BookParagraphDataTransformer parent;
    private ParagraphData data;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private GatheredLostItem gatheredLostItem;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        node = mockControl.createMock(Node.class);
        data = mockControl.createMock(ParagraphData.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        gatheredLostItem = mockControl.createMock(GatheredLostItem.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new GatherItemTransformer();
        underTest.setBeanFactory(beanFactory);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenParentIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(null, node, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenNodeIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, null, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenDataIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, null);
        // THEN throws exception
    }

    public void testTransformWhenAmountIsSetShouldGatherAndSetInfo() {
        // GIVEN
        expectAttribute("id", ID);
        expectAttribute("amount", AMOUNT_STRING);
        expectAttribute("dose");
        expectAttribute("unequippedOnly");
        expect(beanFactory.getBean("gatheredLostItem", ID, AMOUNT, 0, false)).andReturn(gatheredLostItem);

        data.addGatheredItem(gatheredLostItem);

        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        Assert.assertTrue(true);
    }

    public void testTransformWhenDoseIsSetShouldGatherAndSetInfo() {
        // GIVEN
        expectAttribute("id", ID);
        expectAttribute("amount", "0");
        expectAttribute("dose", "3");
        expectAttribute("unequippedOnly");
        expect(beanFactory.getBean("gatheredLostItem", ID, 0, 3, false)).andReturn(gatheredLostItem);
        data.addGatheredItem(gatheredLostItem);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        Assert.assertTrue(true);
    }

    public void testTransformWhenAmountIsNotSetShouldGatherAndSetInfoWithOneAmount() {
        // GIVEN
        expectAttribute("id", ID);
        expectAttribute("amount");
        expectAttribute("dose");
        expectAttribute("unequippedOnly");
        expect(beanFactory.getBean("gatheredLostItem", ID, 1, 0, false)).andReturn(gatheredLostItem);

        data.addGatheredItem(gatheredLostItem);

        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        Assert.assertTrue(true);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
