package hu.zagor.gamebooks.controller.image;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.controller.domain.ImageLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ClasspathImageHandler}.
 * @author Tamas_Szekeres
 */
@Test
@PrepareForTest(IOUtils.class)
public class ClasspathImageHandlerNegativeTest {

    private static final String HU = "hu";
    private static final String FILE = "fileName";
    private static final String DIR = "dirName";
    private ClasspathImageHandler underTest;
    private IMocksControl mockControl;
    private Logger logger;
    private OutputStream response;
    private ImageLocation imageLocation;
    private Locale locale;
    private ImageLookupStrategy strategy;
    private Resource resource;
    private InputStream inputStream;

    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new PowerMockObjectFactory();
    }

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        response = mockControl.createMock(OutputStream.class);
        imageLocation = mockControl.createMock(ImageLocation.class);
        strategy = mockControl.createMock(ImageLookupStrategy.class);
        resource = mockControl.createMock(Resource.class);
        inputStream = mockControl.createMock(InputStream.class);
        locale = new Locale(HU);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ClasspathImageHandler();
        final Map<ImageLookupStrategyType, ImageLookupStrategy> lookupStrategies = new HashMap<ImageLookupStrategyType, ImageLookupStrategy>();
        lookupStrategies.put(ImageLookupStrategyType.BW_COLOR, strategy);
        underTest.setLookupStrategies(lookupStrategies);
        Whitebox.setInternalState(underTest, "logger", logger);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleImageWhenResponseIsNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleImage(null, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleImageWhenImageLocationIsNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, null, ImageLookupStrategyType.COLOR_BW, false);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleImageWhenLookupStrategyIsNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, null, false);
        // THEN throws exception
    }

    @Test(expectedExceptions = IOException.class)
    public void testHandleImageWhenStreamCopyFailsShouldCloseInputStreamAndThrowException() throws IOException {
        // GIVEN
        expect(imageLocation.getLocale()).andReturn(locale);
        expect(imageLocation.getFile()).andReturn(FILE);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking for image '{}' for book '{}' for locale '{}_{}'.", FILE, DIR, HU, "");
        expect(imageLocation.getFile()).andReturn(FILE);

        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking in media module.");
        expect(strategy.getImageResourcesFromDir(DIR, FILE)).andReturn(new Resource[]{resource});

        expect(resource.getInputStream()).andReturn(inputStream);
        PowerMock.mockStatic(IOUtils.class);
        expect(IOUtils.copy(inputStream, response)).andThrow(new IOException());
        inputStream.close();
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN throws exception
    }

    @Test(expectedExceptions = IOException.class)
    public void testHandleImageWhenStreamCopyAndCloseFailsShouldThrowException() throws IOException {
        // GIVEN
        expect(imageLocation.getLocale()).andReturn(locale);
        expect(imageLocation.getFile()).andReturn(FILE);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking for image '{}' for book '{}' for locale '{}_{}'.", FILE, DIR, HU, "");
        expect(imageLocation.getFile()).andReturn(FILE);

        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking in media module.");
        expect(strategy.getImageResourcesFromDir(DIR, FILE)).andReturn(new Resource[]{resource});

        expect(resource.getInputStream()).andReturn(inputStream);
        PowerMock.mockStatic(IOUtils.class);
        expect(IOUtils.copy(inputStream, response)).andThrow(new IOException());
        inputStream.close();
        expectLastCall().andThrow(new IOException());
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN throws exception
    }

    @Test(expectedExceptions = IOException.class)
    public void testHandleImageWhenStreamOpeningFailsShouldThrowException() throws IOException {
        // GIVEN
        expect(imageLocation.getLocale()).andReturn(locale);
        expect(imageLocation.getFile()).andReturn(FILE);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking for image '{}' for book '{}' for locale '{}_{}'.", FILE, DIR, HU, "");
        expect(imageLocation.getFile()).andReturn(FILE);

        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking in media module.");
        expect(strategy.getImageResourcesFromDir(DIR, FILE)).andReturn(new Resource[]{resource});

        expect(resource.getInputStream()).andThrow(new IOException());
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN throws exception
    }

    @Test(expectedExceptions = IOException.class)
    public void testHandleImageWhenStreamClosingFailsShouldThrowException() throws IOException {
        // GIVEN
        expect(imageLocation.getLocale()).andReturn(locale);
        expect(imageLocation.getFile()).andReturn(FILE);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking for image '{}' for book '{}' for locale '{}_{}'.", FILE, DIR, HU, "");
        expect(imageLocation.getFile()).andReturn(FILE);

        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking in media module.");
        expect(strategy.getImageResourcesFromDir(DIR, FILE)).andReturn(new Resource[]{resource});

        expect(resource.getInputStream()).andReturn(inputStream);
        PowerMock.mockStatic(IOUtils.class);
        expect(IOUtils.copy(inputStream, response)).andReturn(1000);
        inputStream.close();
        expectLastCall().andThrow(new IOException());
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN throws exception
    }

    @Test(expectedExceptions = IOException.class)
    public void testHandleImageWhenStreamOpeningReturnsNullShouldThrowExceptionAtCopying() throws IOException {
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
        PowerMock.mockStatic(IOUtils.class);
        expect(IOUtils.copy((InputStream) null, response)).andThrow(new IOException());
        PowerMock.replay(IOUtils.class);
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
