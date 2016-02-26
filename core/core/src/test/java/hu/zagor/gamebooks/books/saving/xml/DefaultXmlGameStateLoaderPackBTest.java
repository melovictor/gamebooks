package hu.zagor.gamebooks.books.saving.xml;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.startsWith;
import hu.zagor.gamebooks.books.saving.xml.exception.UnknownFieldTypeException;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNumbers;
import hu.zagor.gamebooks.support.logging.LoggerInjector;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
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
    @UnderTest private DefaultXmlGameStateLoader underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private Logger logger;
    @Inject private AutowireCapableBeanFactory beanFactory;
    private DocumentBuilderFactory builderFactory;
    @Inject private LoggerInjector loggerInjector;
    @Inject private ClassFieldFilter classFieldFilter;

    @BeforeClass
    public void setUpClass() {
        builderFactory = DocumentBuilderFactory.newInstance();
        Whitebox.setInternalState(underTest, "builderFactory", builderFactory);
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
        final Object returned = underTest.load(input);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadWhenNonexistentFieldIsSetShouldReturnNull() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumbers</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNumbers\">" + "<intBadField class=\"java.lang.Integer\">0</intBadField>"
            + "</value>" + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 3);
        expect(classFieldFilter.isIgnorableField(anyObject(SimpleClassWithNumbers.class), eq("intBadField"))).andReturn(false);
        logger.error(eq("Failed to load saved game, the deserializer threw an exception."), anyObject(UnknownFieldTypeException.class));
        mockControl.replay();
        // WHEN
        final Object returned = underTest.load(input);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadWhenNonexistentIgnorableFieldIsSetShouldReturnParsedObject() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumbers</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNumbers\">" + "<intBadField class=\"java.lang.Integer\">0</intBadField>"
            + "<longField class=\"java.lang.Long\">96</longField>" + "</value>" + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 3);
        expect(classFieldFilter.isIgnorableField(anyObject(SimpleClassWithNumbers.class), eq("intBadField"))).andReturn(true);
        mockControl.replay();
        // WHEN
        final Object returned = underTest.load(input);
        // THEN
        Assert.assertTrue(returned instanceof HashMap);
        @SuppressWarnings("unchecked")
        final Map<String, Serializable> loadedMap = (HashMap<String, Serializable>) returned;
        Assert.assertTrue(loadedMap.containsKey("fieldWithNumbers"));
        final SimpleClassWithNumbers actual = (SimpleClassWithNumbers) loadedMap.get("fieldWithNumbers");
        Assert.assertEquals(actual.getLongField(), 96L);
    }

    public void testLoadWhenNonHandledSimpleTypeIsSetShouldReturnNull() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.Byte\">fieldWithNumbers</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNumbers\">" + "<intBadField class=\"java.lang.Integer\">0</intBadField>"
            + "</value>" + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 2);
        logger.error(eq("Failed to load saved game, the deserializer threw an exception."), anyObject(UnknownFieldTypeException.class));
        mockControl.replay();
        // WHEN
        final Object returned = underTest.load(input);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadWhenInputContainsObjectShouldReturnNull() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">note</key>"
            + "<value class=\"java.lang.Object\">I am a note here and everywhere.</value>" + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 2);
        logger.error(eq("Failed to load saved game, the deserializer threw an exception."), anyObject(UnknownFieldTypeException.class));
        mockControl.replay();
        // WHEN
        final Object returned = underTest.load(input);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadWhenInputContainsClassWithNumberAndWithReferencesShouldReturnProperMap() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumber</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt\" ref=\"99\">" + "<intField class=\"java.lang.Integer\">1535</intField>"
            + "</value>" + "</mapEntry>" + "<mapEntry>" + "<key class=\"java.lang.String\">otherFieldWithNumber</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt\" ref=\"37\">" + "<intField class=\"java.lang.Integer\">11</intField>"
            + "</value>" + "</mapEntry>" + "<mapEntry>" + "<key class=\"java.lang.String\">repeatingFieldWithNumber</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt\" ref=\"99\" />" + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 4);
        mockControl.replay();
        // WHEN
        final Object returned = underTest.load(input);
        // THEN
        Assert.assertTrue(returned instanceof HashMap);
        @SuppressWarnings("unchecked")
        final Map<String, Serializable> loadedMap = (HashMap<String, Serializable>) returned;

        verifyClassWithNumber(loadedMap, "fieldWithNumber", 1535);
        verifyClassWithNumber(loadedMap, "otherFieldWithNumber", 11);
        verifyClassWithNumber(loadedMap, "repeatingFieldWithNumber", 1535);
        Assert.assertSame(loadedMap.get("repeatingFieldWithNumber"), loadedMap.get("fieldWithNumber"));
    }

    public void testLoadWhenInputContainsClassWithNumberAndWithReferencesButFailsToFindSetterWithCorrectParametersShouldReturnNull() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumber</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt\" ref=\"99\">" + "<intField class=\"java.lang.Double\">1535</intField>"
            + "</value>" + "</mapEntry>" + "<mapEntry>" + "<key class=\"java.lang.String\">otherFieldWithNumber</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt\" ref=\"37\">" + "<intField class=\"java.lang.Integer\">11</intField>"
            + "</value>" + "</mapEntry>" + "<mapEntry>" + "<key class=\"java.lang.String\">repeatingFieldWithNumber</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt\" ref=\"99\" />" + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 3);
        logger.error("Couldn't find setter method '{}' in class '{}' for type '{}'.", "setIntField", SimpleClassWithInt.class, Double.class);
        logger.error(eq("Failed to load saved game, the deserializer threw an exception."), anyObject(IllegalStateException.class));
        mockControl.replay();
        // WHEN
        final Object returned = underTest.load(input);
        // THEN
        Assert.assertNull(returned);
    }

    private void verifyClassWithNumber(final Map<String, Serializable> loadedMap, final String key, final int value) {
        Assert.assertTrue(loadedMap.containsKey(key));
        final Serializable serializable = loadedMap.get(key);
        Assert.assertTrue(serializable instanceof SimpleClassWithInt);
        final SimpleClassWithInt intClass = (SimpleClassWithInt) serializable;
        Assert.assertEquals(intClass.getIntField(), value);
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
