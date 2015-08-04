package hu.zagor.gamebooks.books.saving.xml;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.startsWith;
import hu.zagor.gamebooks.books.saving.xml.exception.UnknownFieldTypeException;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithList;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNumbers;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleEnum;
import hu.zagor.gamebooks.support.logging.LoggerInjector;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.xml.sax.SAXException;

/**
 * Unit test for class {@link DefaultXmlGameStateLoader}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultXmlGameStateLoaderPackATest {

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

    public void testLoadWhenInputContainsOnlyStringShouldReturnProperMap() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">note</key>"
            + "<value class=\"java.lang.String\">I am a note here and everywhere.</value>" + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 2);
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertTrue(loadedObject instanceof HashMap);
        @SuppressWarnings("unchecked")
        final Map<String, Serializable> loadedMap = (HashMap<String, Serializable>) loadedObject;
        Assert.assertTrue(loadedMap.containsKey("note"));
        Assert.assertEquals(loadedMap.get("note"), "I am a note here and everywhere.");
    }

    public void testLoadWhenInputContainsClassWithNumberShouldReturnProperMap() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumber</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt\">" + "<intField class=\"java.lang.Integer\">1535</intField>" + "</value>"
            + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 3);
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertTrue(loadedObject instanceof HashMap);
        @SuppressWarnings("unchecked")
        final Map<String, Serializable> loadedMap = (HashMap<String, Serializable>) loadedObject;
        Assert.assertTrue(loadedMap.containsKey("fieldWithNumber"));
        final Serializable serializable = loadedMap.get("fieldWithNumber");
        Assert.assertTrue(serializable instanceof SimpleClassWithInt);
        final SimpleClassWithInt intClass = (SimpleClassWithInt) serializable;
        Assert.assertEquals(intClass.getIntField(), 1535);
    }

    public void testLoadWhenInputContainsClassWithNumbersShouldReturnProperMap() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumbers</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNumbers\">" + "<intField class=\"java.lang.Integer\">0</intField>"
            + "<longField class=\"java.lang.Long\">3244765413524751234</longField>" + "<doubleField class=\"java.lang.Double\">-3.14156664</doubleField>"
            + "<booleanField class=\"java.lang.Boolean\">false</booleanField>" + "<stringField class=\"java.lang.String\" />" + "</value>" + "</mapEntry>" + "</element>"
            + "</mainObject>";
        prepareForCreation(input, 3);
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertTrue(loadedObject instanceof HashMap);
        @SuppressWarnings("unchecked")
        final Map<String, Serializable> loadedMap = (HashMap<String, Serializable>) loadedObject;
        Assert.assertTrue(loadedMap.containsKey("fieldWithNumbers"));
        final Serializable serializable = loadedMap.get("fieldWithNumbers");
        Assert.assertTrue(serializable instanceof SimpleClassWithNumbers);
        final SimpleClassWithNumbers numbersClass = (SimpleClassWithNumbers) serializable;
        Assert.assertEquals(numbersClass.getIntField(), 0);
        Assert.assertEquals(numbersClass.getLongField(), 3244765413524751234L);
        Assert.assertEquals(numbersClass.getDoubleField(), -3.14156664);
        Assert.assertFalse(numbersClass.isBooleanField());
        Assert.assertEquals(numbersClass.getStringField(), "");
    }

    public void testLoadWhenInputContainsClassWithListShouldReturnProperMap() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithList</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithList\">" + "<elements class=\"java.util.ArrayList\" isList=\"true\">"
            + "<listElement class=\"java.lang.String\">apple</listElement>" + "<listElement class=\"java.lang.String\">pear</listElement>" + "</elements>" + "</value>"
            + "</mapEntry>" + "</element>" + "</mainObject>";
        prepareForCreation(input, 4);
        beanFactory.autowireBean(anyObject(String[].class));
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertTrue(loadedObject instanceof HashMap);
        @SuppressWarnings("unchecked")
        final Map<String, Serializable> loadedMap = (HashMap<String, Serializable>) loadedObject;
        Assert.assertTrue(loadedMap.containsKey("fieldWithList"));
        final Serializable serializable = loadedMap.get("fieldWithList");
        Assert.assertTrue(serializable instanceof SimpleClassWithList);
        final SimpleClassWithList listClass = (SimpleClassWithList) serializable;
        final List<String> listElements = listClass.getElements();
        Assert.assertEquals(listElements.size(), 2);
        Assert.assertEquals(listElements.get(0), "apple");
        Assert.assertEquals(listElements.get(1), "pear");
    }

    public void testLoadWhenInputContainsClassWithEnumShouldReturnProperMap() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithEnum</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum\">"
            + "<enumField class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleEnum\" isEnum=\"true\" value=\"PINEAPPLE\"></enumField>" + "</value>" + "</mapEntry>"
            + "</element>" + "</mainObject>";
        prepareForCreation(input, 3);
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertTrue(loadedObject instanceof HashMap);
        @SuppressWarnings("unchecked")
        final Map<String, Serializable> loadedMap = (HashMap<String, Serializable>) loadedObject;
        Assert.assertTrue(loadedMap.containsKey("fieldWithEnum"));
        final Serializable serializable = loadedMap.get("fieldWithEnum");
        Assert.assertTrue(serializable instanceof SimpleClassWithEnum);
        final SimpleClassWithEnum enumClass = (SimpleClassWithEnum) serializable;
        Assert.assertEquals(enumClass.getEnumField(), SimpleEnum.PINEAPPLE);
    }

    public void testLoadWhenInputContainsClassWithNullEnumShouldReturnProperMap() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithEnum</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum\">"
            + "<enumField class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleEnum\" isEnum=\"true\" isNull=\"true\"></enumField>" + "</value>" + "</mapEntry>"
            + "</element>" + "</mainObject>";
        prepareForCreation(input, 3);
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertTrue(loadedObject instanceof HashMap);
        @SuppressWarnings("unchecked")
        final Map<String, Serializable> loadedMap = (HashMap<String, Serializable>) loadedObject;
        Assert.assertTrue(loadedMap.containsKey("fieldWithEnum"));
        final Serializable serializable = loadedMap.get("fieldWithEnum");
        Assert.assertTrue(serializable instanceof SimpleClassWithEnum);
        final SimpleClassWithEnum enumClass = (SimpleClassWithEnum) serializable;
        Assert.assertNull(enumClass.getEnumField());
    }

    public void testLoadWhenInputContainsClassWithNullShouldReturnProperMap() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNull</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull\">" + "<nullField isNull=\"true\"></nullField>" + "</value>" + "</mapEntry>"
            + "</element>" + "</mainObject>";
        prepareForCreation(input, 3);
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertTrue(loadedObject instanceof HashMap);
        @SuppressWarnings("unchecked")
        final Map<String, Serializable> loadedMap = (HashMap<String, Serializable>) loadedObject;
        Assert.assertTrue(loadedMap.containsKey("fieldWithNull"));
        final Serializable serializable = loadedMap.get("fieldWithNull");
        Assert.assertTrue(serializable instanceof SimpleClassWithNull);
        final SimpleClassWithNull enumClass = (SimpleClassWithNull) serializable;
        Assert.assertNull(enumClass.getNullField());
    }

    public void testLoadWhenSomethingBreaksShouldReturnNull() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNull</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull\">" + "<nullField isNull=\"true\"></nullField>" + "</value>" + "</mapEntry>"
            + "</element>" + "</mainObject>";
        expectBeanFactoryCalls(input);
        logger.error(eq("Failed to load saved game, the deserializer threw an exception."), anyObject(SAXException.class));
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertNull(loadedObject);
    }

    public void testLoadWhenInputContainsClassWithWrongNumberShouldReturnNull() {
        // GIVEN
        final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumbers</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNumbers\">" + "<intField class=\"java.lang.Short\">0</intField>" + "</value>"
            + "</mapEntry>" + "</element>" + "</mainObject>";
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
