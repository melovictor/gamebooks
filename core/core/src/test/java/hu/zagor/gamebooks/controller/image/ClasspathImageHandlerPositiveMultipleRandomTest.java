package hu.zagor.gamebooks.controller.image;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.imagetype.ImageTypeDetector;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import hu.zagor.gamebooks.support.stream.IoUtilsWrapper;
import hu.zagor.gamebooks.support.url.LastModificationTimeResolver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.Resource;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ClasspathImageHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class ClasspathImageHandlerPositiveMultipleRandomTest {

    private static final String HU = "hu";
    private static final String FILE = "fileName.jpg";
    private static final String COVER = "cover";
    private static final String DIR = "dirName";
    private static final String DIRLOCALE = "dirLocaleName";
    @UnderTest private ClasspathImageHandler underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private Logger logger;
    @Mock private ServletOutputStream outputStream;
    @Mock private HttpServletResponse response;
    @Mock private HttpServletRequest request;
    @Mock private ImageLocation imageLocation;
    private Locale locale;
    @Mock private ImageLookupStrategy strategy;
    @Mock private Resource resource;
    @Mock private Resource resource2;
    @Mock private InputStream inputStream;
    @Inject private RandomNumberGenerator generator;
    @Inject private IoUtilsWrapper ioUtilsWrapper;
    @Mock private File file;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSession session;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private PlayerUser player;
    @Mock private PlayerSettings settings;
    @Mock private URLConnection connection;
    @Inject private LastModificationTimeResolver lastModificationTimeResolver;
    @Inject private ImageTypeDetector imageTypeDetector;

    @BeforeClass
    public void setUpClass() {
        locale = new Locale(HU);
    }

    @BeforeMethod
    public void setUpMethod() {
        final Map<ImageLookupStrategyType, ImageLookupStrategy> lookupStrategies = new HashMap<ImageLookupStrategyType, ImageLookupStrategy>();
        lookupStrategies.put(ImageLookupStrategyType.BW_COLOR, strategy);
        underTest.setLookupStrategies(lookupStrategies);
    }

    public void testHandleImageWhenLookupReturnsMoreThanOneLanguageImageAndRandomIsOnShouldCopyTheIndexedOneToOutput() throws IOException {
        // GIVEN
        expectWrapper();
        getStrategyType();
        expect(imageLocation.getLocale()).andReturn(locale);
        expect(imageLocation.getFile()).andReturn(FILE);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking for image '{}' for book '{}' for locale '{}_{}'.", FILE, DIR, HU, "");
        expect(imageLocation.getFile()).andReturn(FILE);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking in media module.");
        expect(strategy.getImageResourcesFromDir(DIR, FILE)).andReturn(new Resource[0]);
        expect(imageLocation.getFullDirLocale()).andReturn(DIRLOCALE);
        logger.debug("Looking in language_country module.");
        expect(strategy.getImageResourcesFromDir(DIRLOCALE, FILE)).andReturn(new Resource[]{resource, resource2});
        expect(generator.getRandomNumber(1, 2, -1)).andReturn(new int[]{1});

        expect(request.getDateHeader("If-Modified-Since")).andReturn(-1L);
        expect(lastModificationTimeResolver.getLastModified(resource2)).andReturn(999L);

        expect(response.getOutputStream()).andReturn(outputStream);
        expect(resource2.getInputStream()).andReturn(inputStream);

        expect(imageTypeDetector.probeContentType(inputStream, outputStream)).andReturn("image/jpeg");
        response.addHeader("Content-Type", "image/jpeg");
        expect(resource2.contentLength()).andReturn(7777L);
        response.setContentLength(7777);

        expect(ioUtilsWrapper.copy(inputStream, outputStream)).andReturn(1000);
        inputStream.close();
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, imageLocation, true);
        // THEN
    }

    public void testHandleImageWhenImageIsCachedButNeedRandomShouldSearchInLanguageOnlyAndCopyToOutput() throws IOException {
        // GIVEN
        expectWrapper();
        getStrategyType();
        expect(imageLocation.getLocale()).andReturn(locale);
        expect(imageLocation.getFile()).andReturn(COVER);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking for image '{}' for book '{}' for locale '{}_{}'.", COVER, DIR, HU, "");
        expect(imageLocation.getFile()).andReturn(COVER);
        expect(imageLocation.getFullDirLocale()).andReturn(DIRLOCALE);
        logger.debug("Looking in language_country module.");
        expect(strategy.getImageResourcesFromDir(DIRLOCALE, COVER)).andReturn(new Resource[]{});
        expect(imageLocation.getDirLocale()).andReturn(DIRLOCALE);
        logger.debug("Looking in language module.");
        expect(strategy.getImageResourcesFromDir(DIRLOCALE, COVER)).andReturn(new Resource[]{resource, resource2});
        expect(generator.getRandomNumber(1, 2, -1)).andReturn(new int[]{1});
        expect(request.getDateHeader("If-Modified-Since")).andReturn(1442L);
        expect(lastModificationTimeResolver.getLastModified(resource2)).andReturn(999000L);

        expect(response.getOutputStream()).andReturn(outputStream);
        expect(resource2.getInputStream()).andReturn(inputStream);
        expect(imageTypeDetector.probeContentType(inputStream, outputStream)).andReturn("image/jpeg");

        response.addHeader("Content-Type", "image/jpeg");

        expect(resource2.contentLength()).andReturn(7777L);
        response.setContentLength(7777);
        expect(ioUtilsWrapper.copy(inputStream, outputStream)).andReturn(1000);
        inputStream.close();
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, imageLocation, true);
        // THEN
    }

    private void getStrategyType() {
        expect(wrapper.getPlayer()).andReturn(player);
        expect(player.getSettings()).andReturn(settings);
        expect(settings.getImageTypeOrder()).andReturn("bwFirst");
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

}
