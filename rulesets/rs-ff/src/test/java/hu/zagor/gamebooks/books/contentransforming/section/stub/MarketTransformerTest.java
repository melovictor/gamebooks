package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.market.GiveUpMode;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfMarketTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class MarketTransformerTest extends AbstractTransformerTest {
    @MockControl private IMocksControl mockControl;
    @UnderTest private FfMarketTransformer underTest;
    @Inject private BeanFactory beanFactory;
    @Mock private BookParagraphDataTransformer parent;
    @Mock private FfParagraphData data;
    @Mock private ChoicePositionCounter counter;

    @Test(expectedExceptions = IllegalStateException.class)
    public void testWhenParagraphDoesntContainTheMarketMarkerShouldThrowException() {
        // GIVEN
        expectAttribute("suppressWarning");
        expect(data.getText()).andReturn("Some text.");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testWhenParagraphDoesntContainTheMarketMarkerAndSuppressionIsTurnedOffShouldThrowException() {
        // GIVEN
        expectAttribute("suppressWarning", "false");
        expect(data.getText()).andReturn("Some text.");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN throws exception
    }

    public void testWhenSupressedWarningAndNoGiveUpAmountIsSetAndTransformationSucceedsShouldTransformProperly() {
        // GIVEN
        expectAttribute("suppressWarning", "true");
        final Capture<MarketCommand> capture = newCapture();
        data.addCommand(capture(capture));
        expectAttribute("moneyAttribute");
        expectAttribute("currencySingle");
        expectAttribute("currencyMultiple");
        expectAttribute("mustHaveGold");
        expectAttribute("mustSellExactly");
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
        Assert.assertNull(command.getGiveUpMode());
    }

    public void testWhenNoGiveUpAmountIsSetAndTransformationSucceedsShouldTransformProperly() {
        // GIVEN
        expectAttribute("suppressWarning");
        expect(data.getText()).andReturn("Some text. [div data-market=\"\"][/div]");
        final Capture<MarketCommand> capture = newCapture();
        data.addCommand(capture(capture));
        expectAttribute("moneyAttribute");
        expectAttribute("currencySingle");
        expectAttribute("currencyMultiple");
        expectAttribute("mustHaveGold");
        expectAttribute("mustSellExactly");
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
        Assert.assertNull(command.getGiveUpMode());
    }

    public void testWhenGiveUpAmountIsSetAndTransformationSucceedsShouldTransformProperly() {
        // GIVEN
        expectAttribute("suppressWarning");
        expect(data.getText()).andReturn("Some text. [div data-market=\"\"][/div]");
        final Capture<MarketCommand> capture = newCapture();
        data.addCommand(capture(capture));
        expectAttribute("moneyAttribute");
        expectAttribute("currencySingle");
        expectAttribute("currencyMultiple");
        expectAttribute("mustHaveGold", "3");
        expectAttribute("mustSellExactly", "5");
        expectAttribute("mustBuy", "2");
        expectAttribute("mustGiveUp", "2");
        expectAttribute("mustGiveUpMode");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        expect(data.getPositionCounter()).andReturn(counter);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final MarketCommand command = capture.getValue();
        Assert.assertEquals(command.getMoneyAttribute(), "gold");
        Assert.assertEquals(command.getSingleCcyKey(), "page.ff.label.market.goldPiece");
        Assert.assertEquals(command.getMultipleCcyKey(), "page.ff.label.market.goldPieces");
        Assert.assertEquals(command.getMustHaveGold(), 3);
        Assert.assertEquals(command.getGiveUpMode(), GiveUpMode.asMuchAsPossible);
        Assert.assertEquals(command.getMustBuy(), 2);
        Assert.assertEquals(command.getMustSellExactly(), 5);
        Assert.assertEquals(command.getGiveUpAmount(), 2);
    }

    public void testWhenGiveUpAmountAndGiveUpModeAreSetAndTransformationSucceedsShouldTransformProperly() {
        // GIVEN
        expectAttribute("suppressWarning");
        expect(data.getText()).andReturn("Some text. [div data-market=\"\"][/div]");
        final Capture<MarketCommand> capture = newCapture();
        data.addCommand(capture(capture));
        expectAttribute("moneyAttribute", "dollars");
        expectAttribute("currencySingle");
        expectAttribute("currencyMultiple");
        expectAttribute("mustHaveGold", "3");
        expectAttribute("mustSellExactly", "5");
        expectAttribute("mustBuy", "2");
        expectAttribute("mustGiveUp", "2");
        expectAttribute("mustGiveUpMode", "allOrNothing");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        expect(data.getPositionCounter()).andReturn(counter);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final MarketCommand command = capture.getValue();
        Assert.assertEquals(command.getMoneyAttribute(), "dollars");
        Assert.assertEquals(command.getSingleCcyKey(), "page.ff.label.market.goldPiece");
        Assert.assertEquals(command.getMultipleCcyKey(), "page.ff.label.market.goldPieces");
        Assert.assertEquals(command.getMustHaveGold(), 3);
        Assert.assertEquals(command.getGiveUpMode(), GiveUpMode.allOrNothing);
        Assert.assertEquals(command.getMustBuy(), 2);
        Assert.assertEquals(command.getMustSellExactly(), 5);
        Assert.assertEquals(command.getGiveUpAmount(), 2);
    }

}
