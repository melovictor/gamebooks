package hu.zagor.gamebooks.controller.image;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
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
import org.testng.Assert;
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
public class ClasspathImageHandlerPositiveTest {

    private static final String HU = "hu";
    private static final String FILE = "fileName";
    private static final String COVER = "cover";
    private static final String DIR = "dirName";
    private static final String DIRLOCALE = "dirLocaleName";
    private ClasspathImageHandler underTest;
    private IMocksControl mockControl;
    private Logger logger;
    private OutputStream response;
    private ImageLocation imageLocation;
    private Locale locale;
    private ImageLookupStrategy strategy;
    private Resource resource;
    private Resource resource2;
    private InputStream inputStream;
    private RandomNumberGenerator generator;

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
        resource2 = mockControl.createMock(Resource.class);
        inputStream = mockControl.createMock(InputStream.class);
        locale = new Locale(HU);
        generator = mockControl.createMock(RandomNumberGenerator.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ClasspathImageHandler();
        final Map<ImageLookupStrategyType, ImageLookupStrategy> lookupStrategies = new HashMap<ImageLookupStrategyType, ImageLookupStrategy>();
        lookupStrategies.put(ImageLookupStrategyType.BW_COLOR, strategy);
        underTest.setLookupStrategies(lookupStrategies);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "generator", generator);
        mockControl.reset();
    }

    public void testHandleImageWhenLookupReturnsNoImageShouldDoNothing() throws IOException {
        // GIVEN
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
        expect(strategy.getImageResourcesFromDir(DIRLOCALE, FILE)).andReturn(new Resource[0]);
        expect(imageLocation.getDirLocale()).andReturn(DIRLOCALE);
        logger.debug("Looking in language module.");
        expect(strategy.getImageResourcesFromDir(DIRLOCALE, FILE)).andReturn(new Resource[0]);
        logger.warn("Couldn't find requested image.");
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN
        Assert.assertTrue(true);
    }

    public void testHandleImageWhenLookupReturnsLanguageImageShouldCopyItToOutput() throws IOException {
        // GIVEN
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
        expect(strategy.getImageResourcesFromDir(DIRLOCALE, FILE)).andReturn(new Resource[]{});
        expect(imageLocation.getDirLocale()).andReturn(DIRLOCALE);
        logger.debug("Looking in language module.");
        expect(strategy.getImageResourcesFromDir(DIRLOCALE, FILE)).andReturn(new Resource[]{resource});
        expect(resource.getInputStream()).andReturn(inputStream);
        PowerMock.mockStatic(IOUtils.class);
        expect(IOUtils.copy(inputStream, response)).andReturn(1000);
        inputStream.close();
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN
        PowerMock.verify(IOUtils.class);
        Assert.assertTrue(true);
    }

    public void testHandleImageWhenLookupReturnsMoreThanOneLanguageImageShouldCopyTheFirstOneToOutput() throws IOException {
        // GIVEN
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
        expect(resource.getInputStream()).andReturn(inputStream);
        PowerMock.mockStatic(IOUtils.class);
        expect(IOUtils.copy(inputStream, response)).andReturn(1000);
        inputStream.close();
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN
        PowerMock.verify(IOUtils.class);
        Assert.assertTrue(true);
    }

    public void testHandleImageWhenLookupReturnsMoreThanOneLanguageImageAndRandomIsOnShouldCopyTheIndexedOneToOutput() throws IOException {
        // GIVEN
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
        expect(resource2.getInputStream()).andReturn(inputStream);
        PowerMock.mockStatic(IOUtils.class);
        expect(IOUtils.copy(inputStream, response)).andReturn(1000);
        inputStream.close();
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, true);
        // THEN
        PowerMock.verify(IOUtils.class);
        Assert.assertTrue(true);
    }

    public void testHandleImageWhenLookupReturnsMediaImageShouldCopyItToOutput() throws IOException {
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
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN
        PowerMock.verify(IOUtils.class);
        Assert.assertTrue(true);
    }

    public void testHandleImageWhenLookupReturnsMoreThanOneMediaImageShouldCopyTheFirstOneToOutput() throws IOException {
        // GIVEN
        expect(imageLocation.getLocale()).andReturn(locale);
        expect(imageLocation.getFile()).andReturn(FILE);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking for image '{}' for book '{}' for locale '{}_{}'.", FILE, DIR, HU, "");
        expect(imageLocation.getFile()).andReturn(FILE);
        expect(imageLocation.getDir()).andReturn(DIR);
        logger.debug("Looking in media module.");
        expect(strategy.getImageResourcesFromDir(DIR, FILE)).andReturn(new Resource[]{resource, resource2});
        expect(resource.getInputStream()).andReturn(inputStream);
        PowerMock.mockStatic(IOUtils.class);
        expect(IOUtils.copy(inputStream, response)).andReturn(1000);
        inputStream.close();
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN
        PowerMock.verify(IOUtils.class);
        Assert.assertTrue(true);
    }

    public void testHandleImageWhenImageIsCoverShouldSearchInLanguageOnlyAndCopyToOutput() throws IOException {
        // GIVEN
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
        expect(resource.getInputStream()).andReturn(inputStream);
        PowerMock.mockStatic(IOUtils.class);
        expect(IOUtils.copy(inputStream, response)).andReturn(1000);
        inputStream.close();
        PowerMock.replay(IOUtils.class);
        mockControl.replay();
        // WHEN
        underTest.handleImage(response, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        // THEN
        PowerMock.verify(IOUtils.class);
        Assert.assertTrue(true);
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

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
