package hu.zagor.gamebooks.content.command.random;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.ParagraphData;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link RandomResult}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomResultTest {

    private IMocksControl mockControl;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testCloneShouldReturnClone() throws CloneNotSupportedException {
        // GIVEN
        final RandomResult underTest = new RandomResult();
        underTest.setMax("3");
        underTest.setMin("1");
        final ParagraphData paragraphData = mockControl.createMock(ParagraphData.class);
        underTest.setParagraphData(paragraphData);
        final ParagraphData paragraphDataCloned = mockControl.createMock(ParagraphData.class);
        expect(paragraphData.clone()).andReturn(paragraphDataCloned);
        mockControl.replay();
        // WHEN
        final RandomResult returned = underTest.clone();
        // THEN
        Assert.assertEquals(returned.getMin(), underTest.getMin());
        Assert.assertEquals(returned.getMax(), underTest.getMax());
        Assert.assertEquals(returned.getParagraphData(), paragraphDataCloned);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
