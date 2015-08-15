package hu.zagor.gamebooks.ff.ff.votv.mvc.books.newgame.controller;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff38BookNewGameController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38BookNewGameControllerTest {

    private Ff38BookNewGameController underTest;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        beanFactory = mockControl.createMock(BeanFactory.class);
        underTest = new Ff38BookNewGameController();
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testConstructor() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getClass();
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
