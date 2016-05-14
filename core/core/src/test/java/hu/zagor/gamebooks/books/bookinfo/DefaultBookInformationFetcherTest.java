package hu.zagor.gamebooks.books.bookinfo;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import javax.servlet.http.HttpServletRequest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultBookInformationFetcher}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultBookInformationFetcherTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private DefaultBookInformationFetcher underTest;
    @Inject private BeanFactory beanFactory;
    @Mock private BookInformations info;
    @Inject private HttpServletRequest request;

    public void testGetInfoByIdWhenIdIsNotFoundInAppContextShouldReturnNull() {
        // GIVEN
        expect(beanFactory.getBean("info-4", BookInformations.class)).andThrow(new NoSuchBeanDefinitionException("info-4"));
        mockControl.replay();
        // WHEN
        final BookInformations returned = underTest.getInfoById(4);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetInfoByIdWhenIdIsFoundInAppContextShouldReturnInfoInstance() {
        // GIVEN
        expect(beanFactory.getBean("info-2", BookInformations.class)).andReturn(info);
        mockControl.replay();
        // WHEN
        final BookInformations returned = underTest.getInfoById("2");
        // THEN
        Assert.assertSame(returned, info);
    }

    public void testGetInfoByRequestWhenIdIsFoundShouldReturnInfoInstance() {
        // GIVEN
        expect(request.getRequestURI()).andReturn("/gamebooks/book/41/new");
        expect(beanFactory.getBean("info-41", BookInformations.class)).andReturn(info);
        mockControl.replay();
        // WHEN
        final BookInformations returned = underTest.getInfoByRequest();
        // THEN
        Assert.assertSame(returned, info);
    }

}
