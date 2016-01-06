package hu.zagor.gamebooks.controller.image;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import hu.zagor.gamebooks.support.stream.IoUtilsWrapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
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

    @BeforeClass
    public void setUpClass() {
        locale = new Locale(HU);
    }

    @BeforeMethod
    public void setUpMethod() {
        final Map<ImageLookupStrategyType, ImageLookupStrategy> lookupStrategies = new HashMap<ImageLookupStrategyType, ImageLookupStrategy>();
        lookupStrategies.put(ImageLookupStrategyType.BW_COLOR, strategy);
        underTest.setLookupStrategies(lookupStrategies);
        mockControl.reset();
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

        expectResponseHeaderSetupRandom(resource2);

        expect(resource2.getInputStream()).andReturn(inputStream);
        expect(ioUtilsWrapper.copy(inputStream, outputStream)).andReturn(1000);
        inputStream.close();
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, imageLocation, true);
        // THEN
        Assert.assertTrue(true);
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
        expect(resource2.getFile()).andReturn(file);
        expect(file.lastModified()).andReturn(999000L);
        expect(file.getName()).andReturn(FILE);
        response.addHeader("Content-Type", "image/jpg");
        expect(file.length()).andReturn(7777L);
        response.setContentLengthLong(7777L);
        expect(response.getOutputStream()).andReturn(outputStream);

        expect(resource2.getInputStream()).andReturn(inputStream);
        expect(ioUtilsWrapper.copy(inputStream, outputStream)).andReturn(1000);
        inputStream.close();
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, imageLocation, true);
        // THEN
        Assert.assertTrue(true);
    }

    private void expectResponseHeaderSetupRandom(final Resource selectedResource) throws IOException {
        expect(request.getDateHeader("If-Modified-Since")).andReturn(-1L);
        expect(selectedResource.getFile()).andReturn(file);
        expect(file.lastModified()).andReturn(999L);
        expect(file.getName()).andReturn(FILE);
        response.addHeader("Content-Type", "image/jpg");
        expect(file.length()).andReturn(7777L);
        response.setContentLengthLong(7777L);
        expect(response.getOutputStream()).andReturn(outputStream);
    }

    public void testCreateImageLocationWhenParametersAreProperlySetShouldCreateProperImageLocation() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final ImageLocation returned = underTest.createImageLocation(DIR, FILE, locale);
        // THEN
        Assert.assertEquals(returned.getDir(), DIR);
        Assert.assertEquals(returned.getFile(), FILE);
        Assert.assertEquals(returned.getLocale(), locale);
        Assert.assertEquals(returned.getDirLocale(), DIR + HU);
    }

    private void getStrategyType() {
        expect(wrapper.getPlayer()).andReturn(player);
        expect(player.getSettings()).andReturn(settings);
        expect(settings.getImageTypeOrder()).andReturn("bwFirst");
    }

    private void expectWrapper() {
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
