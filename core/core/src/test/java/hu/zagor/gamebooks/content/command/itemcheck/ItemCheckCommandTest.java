package hu.zagor.gamebooks.content.command.itemcheck;

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
 * Unit test for class {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckCommandTest {

    private static final String ID = "3001";
    private static final String CHECK_TYPE = "item";
    private IMocksControl mockControl;
    private ItemCheckCommand underTest;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ItemCheckCommand();
        mockControl.reset();
    }

    public void testToStringShouldReturnCheckTypeStringAndId() {
        // GIVEN
        underTest.setCheckType(CHECK_TYPE);
        underTest.setId(ID);
        mockControl.replay();
        // WHEN
        final String returned = underTest.toString();
        // THEN
        Assert.assertEquals(returned, "ItemCheckCommand: item 3001");
    }

    public void testCloneWhenNothingIsSetNothingIsCloned() throws CloneNotSupportedException {
        // GIVEN
        mockControl.replay();
        // WHEN
        final ItemCheckCommand returned = underTest.clone();
        // THEN
        Assert.assertNull(returned.getId());
        Assert.assertNull(returned.getCheckType());
        Assert.assertNull(returned.getHave());
        Assert.assertNull(returned.getHaveEquipped());
        Assert.assertNull(returned.getDontHave());
        Assert.assertNull(returned.getAfter());
    }

    public void testCloneWhenEverythingIsSetEverythingIsCloned() throws CloneNotSupportedException {
        // GIVEN
        underTest.setId(ID);
        underTest.setCheckType(CHECK_TYPE);

        final ParagraphData have = mockControl.createMock(ParagraphData.class);
        final ParagraphData dontHave = mockControl.createMock(ParagraphData.class);
        final ParagraphData haveEquipped = mockControl.createMock(ParagraphData.class);
        final ParagraphData after = mockControl.createMock(ParagraphData.class);

        final ParagraphData haveCloned = mockControl.createMock(ParagraphData.class);
        final ParagraphData dontHaveCloned = mockControl.createMock(ParagraphData.class);
        final ParagraphData haveEquippedCloned = mockControl.createMock(ParagraphData.class);
        final ParagraphData afterCloned = mockControl.createMock(ParagraphData.class);

        underTest.setHave(have);
        underTest.setDontHave(dontHave);
        underTest.setHaveEquipped(haveEquipped);
        underTest.setAfter(after);

        expect(have.clone()).andReturn(haveCloned);
        expect(haveEquipped.clone()).andReturn(haveEquippedCloned);
        expect(dontHave.clone()).andReturn(dontHaveCloned);
        expect(after.clone()).andReturn(afterCloned);

        mockControl.replay();
        // WHEN
        final ItemCheckCommand returned = underTest.clone();
        // THEN
        Assert.assertEquals(returned.getId(), ID);
        Assert.assertEquals(returned.getCheckType(), CHECK_TYPE);
        Assert.assertEquals(returned.getHave(), haveCloned);
        Assert.assertEquals(returned.getHaveEquipped(), haveEquippedCloned);
        Assert.assertEquals(returned.getDontHave(), dontHaveCloned);
        Assert.assertEquals(returned.getAfter(), afterCloned);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetCommandViewShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getCommandView("raw");
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
