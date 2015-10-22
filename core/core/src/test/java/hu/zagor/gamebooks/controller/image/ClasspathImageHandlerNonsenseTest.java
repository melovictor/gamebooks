package hu.zagor.gamebooks.controller.image;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import hu.zagor.gamebooks.support.stream.IoUtilsWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.testng.annotations.AfterMethod;
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
    private static final String FILE = "fileName";
    private static final String DIR = "dirName";
    @UnderTest
    private ClasspathImageHandler underTest;
    @MockControl
    private IMocksControl mockControl;
    @Inject
    private Logger logger;
    @Mock
    private OutputStream response;
    @Mock
    private ImageLocation imageLocation;
    private Locale locale;
    @Mock
    private ImageLookupStrategy strategy;
    @Mock
    private Resource resource;
    @Inject
    private IoUtilsWrapper ioUtilsWrapper;

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

    public void testHandleImageWhenStreamOpeningReturnsNullAndCopyDoesNotThrowsExceptionShouldNotClose() throws IOException {
        // GIVEN
        expect(imageLocation.getLocale()).andReturn(locale);
        expect(imageLocation.getFile()).andReturn(FILE);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking for image '{}' for book '{}' for locale '{}_{}'.", FILE, DIR, HU, "");
        expect(imageLocation.getFile()).andReturn(FILE);

        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking in media module.");
        expect(strategy.getImageResourcesFromDir(DIR, FILE)).andReturn(new Resource[]{resource});

        expect(resource.getInputStream()).andReturn(null);
        expect(ioUtilsWrapper.copy((InputStream) null, response)).andReturn(1000);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
