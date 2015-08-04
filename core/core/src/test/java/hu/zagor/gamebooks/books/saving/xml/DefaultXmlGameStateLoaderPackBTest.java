package hu.zagor.gamebooks.books.saving.xml;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.startsWith;
import hu.zagor.gamebooks.books.saving.xml.exception.UnknownFieldTypeException;
import hu.zagor.gamebooks.support.logging.LoggerInjector;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;

/**
 * Unit test for class {@link DefaultXmlGameStateLoader}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultXmlGameStateLoaderPackBTest {

    private DefaultXmlGameStateLoader underTest;
    private IMocksControl mockControl;
    private Logger logger;
    private AutowireCapableBeanFactory beanFactory;
    private DocumentBuilderFactory builderFactory;
    private LoggerInjector loggerInjector;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        beanFactory = mockControl.createMock(AutowireCapableBeanFactory.class);
        builderFactory = DocumentBuilderFactory.newInstance();
        loggerInjector = mockControl.createMock(LoggerInjector.class);

        underTest = new DefaultXmlGameStateLoader();
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "builderFactory", builderFactory);
        Whitebox.setInternalState(underTest, "loggerInjector", loggerInjector);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testLoadWhenInputContainsClassWithoutDefaultConstructorShouldReturnNull() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumber</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.ClassWithoutDefaultConstructor\">" + "<intField class=\"java.lang.Integer\">1535</intField>"
            + "</value>" + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 2);
        logger.debug(startsWith("Creating new instance of class "));
        logger.error("Failed to grab default constructor for class '{}'.", "hu.zagor.gamebooks.books.saving.xml.ClassWithoutDefaultConstructor");
        logger.error(eq("Failed to load saved game, the deserializer threw an exception."), anyObject(NoSuchMethodException.class));
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertNull(loadedObject);
    }

    public void testLoadWhenNonexistentFieldIsSetShouldReturnNull() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumbers</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNumbers\">" + "<intBadField class=\"java.lang.Integer\">0</intBadField>"
            + "</value>" + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 3);
        logger.error(eq("Failed to load saved game, the deserializer threw an exception."), anyObject(UnknownFieldTypeException.class));
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertNull(loadedObject);
    }

    private void prepareForCreation(final String input, final int repetition) {
        expectBeanFactoryCalls(input);
        for (int i = 0; i < repetition; i++) {
            logger.debug(startsWith("Creating new instance of class "));
            expect(loggerInjector.postProcessBeforeInitialization(anyObject(), isNull(String.class))).andReturn(null);
        }
    }

    private void expectBeanFactoryCalls(final String input) {
        final StringReader stringReader = new StringReader(input);
        expect(beanFactory.getBean("stringReader", input)).andReturn(stringReader);
        expect(beanFactory.getBean("inputSource", stringReader)).andReturn(new InputSource(stringReader));
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
