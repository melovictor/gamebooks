package hu.zagor.gamebooks.content.command.fight.domain;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.FfParagraphData;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link RoundEvent}.
 * @author Tamas_Szekeres
 */
@Test
public class RoundEventTest {

    private RoundEvent underTest;
    private IMocksControl mockControl;
    private FfParagraphData ffParagraphData;
    private FfParagraphData ffParagraphDataCloned;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        ffParagraphData = mockControl.createMock(FfParagraphData.class);
        ffParagraphDataCloned = mockControl.createMock(FfParagraphData.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new RoundEvent();
        mockControl.reset();
    }

    public void testCloneShouldReturnClone() throws CloneNotSupportedException {
        // GIVEN
        expect(ffParagraphData.clone()).andReturn(ffParagraphDataCloned);
        underTest.setParagraphData(ffParagraphData);
        mockControl.replay();
        // WHEN
        final RoundEvent returned = underTest.clone();
        // THEN
        Assert.assertNotSame(returned, underTest);
        Assert.assertNotSame(returned.getParagraphData(), underTest.getParagraphData());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
