package hu.zagor.gamebooks.mvc.book.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.partialMockBuilder;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.HashMap;
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

    @UnderTest
    public AbstractRequestWrappingController underTest() {
        return partialMockBuilder(AbstractRequestWrappingController.class).createMock(mockControl);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetInfoWhenCannotFindInfoForBookShouldThrowException() {
        // GIVEN
        expect(applicationContext.getBeansOfType(BookInformations.class)).andReturn(new HashMap<String, BookInformations>() {
            {
                put("a", info);
                put("b", info);
                put("c", info);
                put("d", info);
            }
        });
        expect(info.getId()).andReturn(1L).andReturn(2L).andReturn(3L).andReturn(4L);
        mockControl.replay();
        // WHEN
        underTest.getInfo(7L);
        // THEN throws exception
    }

    public void testGetInfoWhenCanFindInfoForBookShouldReturnIt() {
        // GIVEN
        expect(applicationContext.getBeansOfType(BookInformations.class)).andReturn(new HashMap<String, BookInformations>() {
            {
                put("a", info);
                put("b", info);
                put("c", info);
                put("d", info);
            }
        });
        expect(info.getId()).andReturn(1L).andReturn(7L);
        mockControl.replay();
        // WHEN
        final BookInformations returned = underTest.getInfo(7L);
        // THEN
        Assert.assertSame(returned, info);
    }

}
