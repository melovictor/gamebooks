package hu.zagor.gamebooks.controller.image;

import static org.easymock.EasyMock.expect;
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
public class ClasspathImageHandlerNonsenseTest {

    private static final String HU = "hu";
    private static final String FILE = "fileName.jpg";
    private static final String DIR = "dirName";
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
    @Inject private IoUtilsWrapper ioUtilsWrapper;
    @Mock private File file;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSession session;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private PlayerUser player;
    @Mock private PlayerSettings settings;
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

    public void testHandleImageWhenStreamOpeningReturnsNullAndCopyDoesNotThrowsExceptionShouldNotClose() throws IOException {
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
        expect(strategy.getImageResourcesFromDir(DIR, FILE)).andReturn(new Resource[]{resource});

        expect(request.getDateHeader("If-Modified-Since")).andReturn(-1L);
        expect(lastModificationTimeResolver.getLastModified(resource)).andReturn(999L);
        response.addDateHeader("Last-Modified", 999L);

        expect(response.getOutputStream()).andReturn(outputStream);
        expect(resource.getInputStream()).andReturn(null);

        expect(imageTypeDetector.probeContentType(null, outputStream)).andReturn(null);
        response.addHeader("Content-Type", null);

        expect(resource.contentLength()).andReturn(7777L);
        response.setContentLength(7777);
        expect(ioUtilsWrapper.copy((InputStream) null, outputStream)).andReturn(1000);
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, imageLocation, false);
        // THEN throws exception
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
