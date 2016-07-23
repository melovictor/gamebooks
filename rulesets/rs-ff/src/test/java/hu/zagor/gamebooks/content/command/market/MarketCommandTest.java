package hu.zagor.gamebooks.content.command.market;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.AbstractCommandTest;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link MarketCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class MarketCommandTest extends AbstractCommandTest {

    private MarketElement marketElementA;
    private MarketElement marketElementB;
    private MarketElement marketElementC;
    private MarketElement marketElementD;
    private MarketElement marketElementACloned;
    private MarketElement marketElementBCloned;
    private MarketElement marketElementCCloned;
    private MarketElement marketElementDCloned;

    private FfParagraphData emptyHanded;
    private FfParagraphData after;
    private FfParagraphData emptyHandedCloned;
    private FfParagraphData afterCloned;

    private IMocksControl mockControl;
    private MarketCommand underTest;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();

        emptyHanded = mockControl.createMock(FfParagraphData.class);
        after = mockControl.createMock(FfParagraphData.class);

        marketElementA = mockControl.createMock(MarketElement.class);
        marketElementB = mockControl.createMock(MarketElement.class);
        marketElementC = mockControl.createMock(MarketElement.class);
        marketElementD = mockControl.createMock(MarketElement.class);

        emptyHandedCloned = mockControl.createMock(FfParagraphData.class);
        afterCloned = mockControl.createMock(FfParagraphData.class);

        marketElementACloned = mockControl.createMock(MarketElement.class);
        marketElementBCloned = mockControl.createMock(MarketElement.class);
        marketElementCCloned = mockControl.createMock(MarketElement.class);
        marketElementDCloned = mockControl.createMock(MarketElement.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        underTest = new MarketCommand();
    }

    public void testGetSingleCcyKeyShouldReturnSetValue() {
        // GIVEN
        underTest.setSingleCcyKey("single.key");
        mockControl.replay();
        // WHEN
        final String returned = underTest.getSingleCcyKey();
        // THEN
        Assert.assertEquals(returned, "single.key");
    }

    public void testGetGiveUpAmountShouldReturnSetValue() {
        // GIVEN
        underTest.setGiveUpAmount(5);
        mockControl.replay();
        // WHEN
        final int returned = underTest.getGiveUpAmount();
        // THEN
        Assert.assertEquals(returned, 5);
    }

    public void testGetMultipleCcyKeyShouldReturnSetValue() {
        // GIVEN
        underTest.setMultipleCcyKey("multi.key");
        mockControl.replay();
        // WHEN
        final String returned = underTest.getMultipleCcyKey();
        // THEN
        Assert.assertEquals(returned, "multi.key");
    }

    public void testGetValidMoveShouldReturnFinishMarketing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.getValidMove();
        // THEN
        Assert.assertEquals(returned, "finishMarketing");
    }

    public void testGetCommandViewShouldReturnProperCommandView() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final CommandView returned = underTest.getCommandView("ff");
        // THEN
        Assert.assertEquals(returned.getViewName(), "ffMarket");
        final Map<String, Object> model = returned.getModel();
        Assert.assertSame(model.get("marketCommand"), underTest);
        Assert.assertEquals(model.get("ffChoiceHidden"), true);
    }

    public void testCloneShouldReturnClonedElements() throws CloneNotSupportedException {
        // GIVEN
        underTest.setAfter(after);
        underTest.setEmptyHanded(emptyHanded);
        underTest.getItemsForPurchase().add(marketElementA);
        underTest.getItemsForPurchase().add(marketElementB);
        underTest.getItemsForSale().add(marketElementC);
        underTest.getItemsForSale().add(marketElementD);

        expectTc(after, afterCloned);
        expectTc(emptyHanded, emptyHandedCloned);

        expectTc(marketElementC, marketElementCCloned);
        expectTc(marketElementD, marketElementDCloned);
        expectTc(marketElementA, marketElementACloned);
        expectTc(marketElementB, marketElementBCloned);

        mockControl.replay();
        // WHEN
        final MarketCommand returned = underTest.clone();
        // THEN
        Assert.assertSame(returned.getAfter(), afterCloned);
        Assert.assertSame(returned.getEmptyHanded(), emptyHandedCloned);

        Assert.assertSame(returned.getItemsForPurchase().get(0), marketElementACloned);
        Assert.assertSame(returned.getItemsForPurchase().get(1), marketElementBCloned);

        Assert.assertSame(returned.getItemsForSale().get(0), marketElementCCloned);
        Assert.assertSame(returned.getItemsForSale().get(1), marketElementDCloned);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
