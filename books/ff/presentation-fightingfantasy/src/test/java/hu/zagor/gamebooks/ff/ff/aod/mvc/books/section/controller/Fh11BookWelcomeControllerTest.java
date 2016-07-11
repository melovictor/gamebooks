package hu.zagor.gamebooks.ff.ff.aod.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazatZagor;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Fh11BookWelcomeController}.
 * @author Tamas_Szekeres
 */
@Test
public class Fh11BookWelcomeControllerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Fh11BookWelcomeController underTest;

    public void testHandleWelcomeShouldRedirectToKjkz26() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleWelcome();
        // THEN
        Assert.assertEquals(returned, "redirect:../" + KalandJatekKockazatZagor.A_HALAL_SEREGEI + "/" + PageAddresses.BOOK_WELCOME);
    }

}
