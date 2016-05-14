package hu.zagor.gamebooks.mvc.book.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.partialMockBuilder;
import hu.zagor.gamebooks.books.bookinfo.BookInformationFetcher;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AbstractRequestWrappingController}.
 * @author Tamas_Szekeres
 */
@Test
public class AbstractRequestWrappingControllerTest {

    @MockControl private IMocksControl mockControl;
    private AbstractRequestWrappingController underTest;
    @Inject private ApplicationContext applicationContext;
    @Mock private BookInformations info;
    @Inject private BookInformationFetcher bookInformationFetcher;

    @UnderTest
    public AbstractRequestWrappingController underTest() {
        return partialMockBuilder(AbstractRequestWrappingController.class).createMock(mockControl);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetInfoWhenCannotFindInfoForBookShouldThrowException() {
        // GIVEN
        expect(bookInformationFetcher.getInfoById(7)).andReturn(null);
        mockControl.replay();
        // WHEN
        underTest.getInfo(7L);
        // THEN throws exception
    }

    public void testGetInfoWhenCanFindInfoForBookShouldReturnIt() {
        // GIVEN
        expect(bookInformationFetcher.getInfoById(7)).andReturn(info);
        mockControl.replay();
        // WHEN
        final BookInformations returned = underTest.getInfo(7L);
        // THEN
        Assert.assertSame(returned, info);
    }

}
