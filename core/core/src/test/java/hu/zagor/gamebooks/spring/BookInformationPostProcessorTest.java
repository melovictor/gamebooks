package hu.zagor.gamebooks.spring;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link BookInformationPostProcessor}.
 * @author Tamas_Szekeres
 */
@Test
public class BookInformationPostProcessorTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private BookInformationPostProcessor underTest;
    @Inject private DefaultListableBeanFactory beanFactory;
    @Mock private RandomNumberGenerator randomNumberGenerator;
    @Mock private BookInformations info;

    public void testPostProcessBeforeInitializationShouldReturnBean() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final Object returned = underTest.postProcessBeforeInitialization(randomNumberGenerator, "randomNumberGenerator");
        // THEN
        Assert.assertSame(returned, randomNumberGenerator);
    }

    public void testPostProcessAfterInitializationWhenBeanIsNotBookInformationShouldDoNothing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final Object returned = underTest.postProcessAfterInitialization(randomNumberGenerator, "randomNumberGenerator");
        // THEN
        Assert.assertSame(returned, randomNumberGenerator);
    }

    public void testPostProcessAfterInitializationWhenBeanIsBookInformationShouldRegisterAliasAndReturnOriginal() {
        // GIVEN
        expect(info.getId()).andReturn(990952001L);
        beanFactory.registerAlias("vkm1Info", "info-990952001");
        mockControl.replay();
        // WHEN
        final Object returned = underTest.postProcessAfterInitialization(info, "vkm1Info");
        // THEN
        Assert.assertSame(returned, info);
    }

}
