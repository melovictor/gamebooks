package hu.zagor.gamebooks.books.contentstorage;

import hu.zagor.gamebooks.io.XmlParser;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ResourceBookContentLoader}.
 * @author Tamas_Szekeres
 */
@Test
public class ResourceBookContentLoaderNegativeTest {

    private ResourceBookContentLoader underTest;
    private IMocksControl mockControl;
    private XmlParser xmlParser;
    private ApplicationContext applicationContext;
    private Logger logger;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();

        xmlParser = mockControl.createMock(XmlParser.class);
        applicationContext = mockControl.createMock(ApplicationContext.class);
        logger = mockControl.createMock(Logger.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ResourceBookContentLoader(xmlParser);
        underTest.setApplicationContext(applicationContext);
        Whitebox.setInternalState(underTest, "logger", logger);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenXmlParserIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new ResourceBookContentLoader(null).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLoadBookContentWhenInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.loadBookContent(null);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
