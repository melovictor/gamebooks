package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link RewardTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class RewardTransformerTest extends AbstractTransformerTest {

    private RewardTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private ParagraphData data;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new RewardTransformer();
        init(mockControl);
        data = new ParagraphData();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoTransformWhenOnlyIdIsSetShouldReadAndSetRewardIdAndSeriesMustBeFalse() {
        // GIVEN
        expectAttribute("id", "ring3");
        expectAttribute("useSeriesId");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        Assert.assertEquals(data.getReward().getId(), "ring3");
        Assert.assertFalse(data.getReward().isSeriesId());
    }

    public void testDoTransformWhenBothIdAndSeriesMarkerIsSetShouldReadAndSetRewardIdAndSeriesMarker() {
        // GIVEN
        expectAttribute("id", "ring3");
        expectAttribute("useSeriesId", "true");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        Assert.assertEquals(data.getReward().getId(), "ring3");
        Assert.assertTrue(data.getReward().isSeriesId());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
