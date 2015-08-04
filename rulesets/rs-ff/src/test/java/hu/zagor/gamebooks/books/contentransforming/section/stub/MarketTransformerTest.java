package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.market.MarketCommand;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link MarketTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class MarketTransformerTest extends AbstractTransformerTest {

    private IMocksControl mockControl;
    private MarketTransformer underTest;
    private BeanFactory beanFactory;
    private BookParagraphDataTransformer parent;
    private FfParagraphData data;
    private ChoicePositionCounter counter;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        init(mockControl);
        beanFactory = mockControl.createMock(BeanFactory.class);
        node = mockControl.createMock(Node.class);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        data = mockControl.createMock(FfParagraphData.class);
        counter = mockControl.createMock(ChoicePositionCounter.class);

        underTest = new MarketTransformer();
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testWhenParagraphDoesntContainTheMarketMarkerShouldThrowException() {
        // GIVEN
        expect(data.getText()).andReturn("Some text.");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN throws exception
    }

    public void testWhenNoGiveUpAmountIsSetAndTransformationSucceedsShouldTransformProperly() {
        // GIVEN
        expect(data.getText()).andReturn("Some text. [div data-market=\"\"][/div]");
        final Capture<MarketCommand> capture = new Capture<>();
        data.addCommand(capture(capture));
        expectAttribute("currencySimple");
        expectAttribute("currencyMultiple");
        expectAttribute("mustHaveGold");
        expectAttribute("mustBuy");
        expectAttribute("mustGiveUp");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        expect(data.getPositionCounter()).andReturn(counter);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final MarketCommand command = capture.getValue();
        Assert.assertEquals(command.getSingleCcyKey(), "page.ff.label.market.goldPiece");
        Assert.assertEquals(command.getMultipleCcyKey(), "page.ff.label.market.goldPieces");
        Assert.assertEquals(command.getMustHaveGold(), 0);
        Assert.assertEquals(command.getGiveUpAmount(), 0);
        Assert.assertEquals(command.getMustBuy(), 0);
        Assert.assertFalse(command.isGiveUpMode());
    }

    public void testWhenGiveUpAmountIsSetAndTransformationSucceedsShouldTransformProperly() {
        // GIVEN
        expect(data.getText()).andReturn("Some text. [div data-market=\"\"][/div]");
        final Capture<MarketCommand> capture = new Capture<>();
        data.addCommand(capture(capture));
        expectAttribute("currencySimple");
        expectAttribute("currencyMultiple");
        expectAttribute("mustHaveGold", "3");
        expectAttribute("mustBuy", "2");
        expectAttribute("mustGiveUp", "2");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        expect(data.getPositionCounter()).andReturn(counter);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final MarketCommand command = capture.getValue();
        Assert.assertEquals(command.getSingleCcyKey(), "page.ff.label.market.goldPiece");
        Assert.assertEquals(command.getMultipleCcyKey(), "page.ff.label.market.goldPieces");
        Assert.assertEquals(command.getMustHaveGold(), 3);
        Assert.assertTrue(command.isGiveUpMode());
        Assert.assertEquals(command.getMustBuy(), 2);
        Assert.assertEquals(command.getGiveUpAmount(), 2);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
