package hu.zagor.gamebooks.character.handler.luck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;

import javax.servlet.http.HttpServletRequest;

import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff23BattleLuckTestParameters}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff23BattleLuckTestParametersTest {

    @MockControl
    private IMocksControl mockControl;
    @UnderTest
    private Ff23BattleLuckTestParameters underTest;
    @Inject
    private HttpServletRequest request;

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testGetLuckyAttackAdditionWhenFightingAgainstMorganaShouldReturnOne() {
        // GIVEN
        expect(request.getParameter("id")).andReturn("42");
        mockControl.replay();
        // WHEN
        final int returned = underTest.getLuckyAttackAddition();
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testGetLuckyAttackAdditionWhenNotFightingAgainstMorganaShouldReturnTwo() {
        // GIVEN
        expect(request.getParameter("id")).andReturn("22");
        mockControl.replay();
        // WHEN
        final int returned = underTest.getLuckyAttackAddition();
        // THEN
        Assert.assertEquals(returned, 2);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
