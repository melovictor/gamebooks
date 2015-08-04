package hu.zagor.gamebooks.controller.image;

import static org.easymock.EasyMock.expect;

import java.io.IOException;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link BwFirstImageLookupStrategy}.
 * @author Tamas_Szekeres
 */
@Test
public class BwFirstImageLookupStrategyTest {

    private static final ByteArrayResource[] EMPTY_ARRAY = new ByteArrayResource[0];
    private static final ByteArrayResource[] FILLED_ARRAY = new ByteArrayResource[10];
    private static final String FILE_NAME = "111";
    private static final String COVER_NAME = "cover";
    private static final String DIR = "dir";

    private BwFirstImageLookupStrategy underTest;
    private IMocksControl mockControl;

    private Logger logger;
    private ApplicationContext applicationContext;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        applicationContext = mockControl.createMock(ApplicationContext.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new BwFirstImageLookupStrategy();
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.setApplicationContext(applicationContext);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetImageResourcesFromDirWhenDirIsNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getImageResourcesFromDir(null, FILE_NAME);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetImageResourcesFromDirWhenFileIsNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getImageResourcesFromDir(DIR, null);
        // THEN throws exception
    }

    public void testGetImageResourcesFromDirWhenFileIsCoverShouldCheckAmongCommonImagesOnly() throws IOException {
        // GIVEN
        logger.debug("Looking for common image...");
        final String resourcePath = "classpath*:/" + DIR + "/" + COVER_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(FILLED_ARRAY);
        mockControl.replay();
        // WHEN
        final Resource[] returned = underTest.getImageResourcesFromDir(DIR, COVER_NAME);
        // THEN
        Assert.assertSame(returned, FILLED_ARRAY);
    }

    public void testGetImageResourcesFromDirWhenFileIsAmongBwShouldCheckAmongBwImagesOnly() throws IOException {
        // GIVEN
        logger.debug("Looking for black and white image...");
        final String resourcePath = "classpath*:/" + DIR + "/b-" + FILE_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(FILLED_ARRAY);
        mockControl.replay();
        // WHEN
        final Resource[] returned = underTest.getImageResourcesFromDir(DIR, FILE_NAME);
        // THEN
        Assert.assertSame(returned, FILLED_ARRAY);
    }

    public void testGetImageResourcesFromDirWhenFileIsAmongColorShouldCheckAmongBwAndColorImagesOnly() throws IOException {
        // GIVEN
        logger.debug("Looking for black and white image...");
        String resourcePath = "classpath*:/" + DIR + "/b-" + FILE_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(EMPTY_ARRAY);

        logger.debug("Looking for coloured image...");
        resourcePath = "classpath*:/" + DIR + "/c-" + FILE_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(FILLED_ARRAY);
        mockControl.replay();
        // WHEN
        final Resource[] returned = underTest.getImageResourcesFromDir(DIR, FILE_NAME);
        // THEN
        Assert.assertSame(returned, FILLED_ARRAY);
    }

    public void testGetImageResourcesFromDirWhenFileIsAmongCommonShouldCheckAmongAllImages() throws IOException {
        // GIVEN
        logger.debug("Looking for black and white image...");
        String resourcePath = "classpath*:/" + DIR + "/b-" + FILE_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(EMPTY_ARRAY);

        logger.debug("Looking for coloured image...");
        resourcePath = "classpath*:/" + DIR + "/c-" + FILE_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(EMPTY_ARRAY);

        logger.debug("Looking for common image...");
        resourcePath = "classpath*:/" + DIR + "/" + FILE_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(FILLED_ARRAY);
        mockControl.replay();
        // WHEN
        final Resource[] returned = underTest.getImageResourcesFromDir(DIR, FILE_NAME);
        // THEN
        Assert.assertSame(returned, FILLED_ARRAY);
    }

    public void testGetImageResourcesFromDirWhenFileIsNotAvailableShouldReturnEmptyResources() throws IOException {
        // GIVEN
        logger.debug("Looking for black and white image...");
        String resourcePath = "classpath*:/" + DIR + "/b-" + FILE_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(EMPTY_ARRAY);

        logger.debug("Looking for coloured image...");
        resourcePath = "classpath*:/" + DIR + "/c-" + FILE_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(EMPTY_ARRAY);

        logger.debug("Looking for common image...");
        resourcePath = "classpath*:/" + DIR + "/" + FILE_NAME + ".jpg";
        logger.debug("Looking for image as {}", resourcePath);
        expect(applicationContext.getResources(resourcePath)).andReturn(EMPTY_ARRAY);
        mockControl.replay();
        // WHEN
        final Resource[] returned = underTest.getImageResourcesFromDir(DIR, FILE_NAME);
        // THEN
        Assert.assertSame(returned, EMPTY_ARRAY);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
