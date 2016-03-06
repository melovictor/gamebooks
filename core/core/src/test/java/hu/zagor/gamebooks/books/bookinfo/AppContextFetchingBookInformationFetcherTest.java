package hu.zagor.gamebooks.books.bookinfo;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AppContextFetchingBookInformationFetcher}.
 * @author Tamas_Szekeres
 */
@Test
public class AppContextFetchingBookInformationFetcherTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private AppContextFetchingBookInformationFetcher underTest;
    @Inject private ApplicationContext applicationContext;
    @Instance private Map<String, BookInformations> bookInformations;
    @Mock private BookInformations infoA;
    @Mock private BookInformations infoB;

    @BeforeClass
    public void setUpClass() {
        bookInformations.put("cyoa1", infoA);
        bookInformations.put("ff35", infoB);
    }

    public void testGetInfoByIdWhenIdIsNotFoundInAppContextShouldReturnNull() {
        // GIVEN
        expect(applicationContext.getBeansOfType(BookInformations.class)).andReturn(bookInformations);
        expect(infoA.getId()).andReturn(1L);
        expect(infoB.getId()).andReturn(2L);
        mockControl.replay();
        // WHEN
        final BookInformations returned = underTest.getInfoById("4");
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetInfoByIdWhenIdIsFoundInAppContextShouldReturnInfoInstance() {
        // GIVEN
        expect(applicationContext.getBeansOfType(BookInformations.class)).andReturn(bookInformations);
        expect(infoA.getId()).andReturn(1L);
        expect(infoB.getId()).andReturn(2L);
        mockControl.replay();
        // WHEN
        final BookInformations returned = underTest.getInfoById("2");
        // THEN
        Assert.assertSame(returned, infoB);
    }

}
