package hu.zagor.gamebooks.books.saving.xml;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.startsWith;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithList;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultXmlGameStateSaver}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultXmlGameStateSaverTest {

    private DefaultXmlGameStateSaver underTest;
    private Logger logger;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private DefaultXmlNodeWriter writer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        writer = mockControl.createMock(DefaultXmlNodeWriter.class);

        underTest = new DefaultXmlGameStateSaver();
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenObjectIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.save(null);
        // THEN throws exception
    }

    public void testSaveWhenInputContainsOnlyStringShouldCreateProperXmlOutput() throws UnsupportedEncodingException, XMLStreamException {
        // GIVEN
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        input.put("note", "I am a note.");
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">note</key>"
            + "<value class=\"java.lang.String\">I am a note.</value>" + "</mapEntry>" + "</element>" + "</mainObject>";

        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "note", "java.lang.String");
        writer.createSimpleNode("value", "I am a note.", "java.lang.String");
        writer.closeNode("mapEntry");

        expectEndMainObject(expected);

        mockControl.replay();
        // WHEN
        final String returned = underTest.save(input);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testSaveWhenInputContainsSimpleClassWithIntegerShouldCreateProperXmlOutput() throws UnsupportedEncodingException, XMLStreamException {
        // GIVEN
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        input.put("fieldWithNumber", new SimpleClassWithInt());
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNumber</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt\">" + "<intField class=\"java.lang.Integer\">1534</intField>" + "</value>"
            + "</mapEntry>" + "</element>" + "</mainObject>";

        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "fieldWithNumber", "java.lang.String");

        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt");
        logger.trace("Saving class hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt");

        logger.trace("Saving field 'intField' with value '1534'.");
        writer.createSimpleNode("intField", "1534", "java.lang.Integer");
        writer.closeNode("value");

        writer.closeNode("mapEntry");

        expectEndMainObject(expected);

        mockControl.replay();
        // WHEN
        final String returned = underTest.save(input);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testSaveWhenInputContainsSimpleClassWithListShouldCreateProperXmlOutput() throws UnsupportedEncodingException, XMLStreamException {
        // GIVEN
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        input.put("fieldWithList", new SimpleClassWithList());
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithList</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithList\">" + "<elements class=\"java.util.ArrayList\" isList=\"true\">"
            + "<listElement class=\"java.lang.String\">apple</listElement>" + "<listElement class=\"java.lang.String\">pear</listElement>" + "</elements>" + "</value>"
            + "</mapEntry>" + "</element>" + "</mainObject>";

        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "fieldWithList", "java.lang.String");

        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithList");
        logger.trace("Saving class hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithList");
        logger.trace("Saving field 'elements' with value '[apple, pear]'.");

        writer.openNode("elements");
        writer.addAttribute("class", "java.util.ArrayList");
        writer.addAttribute("isList", "true");
        writer.createSimpleNode("listElement", "apple", "java.lang.String");
        writer.createSimpleNode("listElement", "pear", "java.lang.String");
        writer.closeNode("elements");

        writer.closeNode("value");
        writer.closeNode("mapEntry");

        expectEndMainObject(expected);

        mockControl.replay();
        // WHEN
        final String returned = underTest.save(input);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testSaveWhenInputContainsSimpleClassWithEnumShouldCreateProperXmlOutput() throws UnsupportedEncodingException, XMLStreamException {
        // GIVEN
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        input.put("fieldWithEnum", new SimpleClassWithEnum());
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithEnum</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum\">"
            + "<enumField class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleEnum\" isEnum=\"true\" value=\"KIWI\"></enumField>" + "</value>" + "</mapEntry>"
            + "</element>" + "</mainObject>";

        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "fieldWithEnum", "java.lang.String");

        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum");
        logger.trace("Saving class hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum");

        logger.trace("Saving field 'enumField' with value 'KIWI'.");

        writer.openNode("enumField");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleEnum");
        writer.addAttribute("isEnum", "true");
        writer.addAttribute("value", "KIWI");
        writer.closeNode("enumField");

        writer.closeNode("value");

        writer.closeNode("mapEntry");

        expectEndMainObject(expected);

        mockControl.replay();
        // WHEN
        final String returned = underTest.save(input);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testSaveWhenInputContainsSimpleClassWithNullShouldCreateProperXmlOutput() throws UnsupportedEncodingException, XMLStreamException {
        // GIVEN
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        input.put("fieldWithNull", new SimpleClassWithNull());
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<mainObject class=\"hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper\">"
            + "<element class=\"java.util.HashMap\" isMap=\"true\">" + "<mapEntry>" + "<key class=\"java.lang.String\">fieldWithNull</key>"
            + "<value class=\"hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull\">" + "<nullField isNull=\"true\"></nullField>" + "</value>" + "</mapEntry>"
            + "</element>" + "</mainObject>";

        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "fieldWithNull", "java.lang.String");

        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull");
        logger.trace("Saving class hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull");

        logger.trace("Saving field 'nullField' with value 'null'.");

        writer.createSimpleNode("nullField");

        writer.closeNode("value");

        writer.closeNode("mapEntry");

        expectEndMainObject(expected);

        mockControl.replay();
        // WHEN
        final String returned = underTest.save(input);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testSaveWhenNodeOpeningFailsShouldLogExceptionAndReturnNull() throws XMLStreamException {
        // GIVEN
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        input.put("fieldWithNull", new SimpleClassWithNull());

        expect(beanFactory.getBean(DefaultXmlNodeWriter.class)).andReturn(writer);
        writer.openNode("mainObject");
        final XMLStreamException exception = new XMLStreamException();
        expectLastCall().andThrow(exception);
        logger.error("Failed to save game, the serializer threw an exception.", exception);

        mockControl.replay();
        // WHEN
        final String returned = underTest.save(input);
        // THEN
        Assert.assertNull(returned);
    }

    private void expectStartMainObject() throws XMLStreamException {
        expect(beanFactory.getBean(DefaultXmlNodeWriter.class)).andReturn(writer);

        writer.openNode("mainObject");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper");
        logger.trace("Saving class hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper");
        logger.trace(startsWith("Saving field 'element' with value '{"));

        writer.openNode("element");
        writer.addAttribute("class", "java.util.HashMap");
        writer.addAttribute("isMap", "true");
    }

    private void expectEndMainObject(final String expected) throws XMLStreamException, UnsupportedEncodingException {
        writer.closeNode("element");

        writer.closeNode("mainObject");
        writer.closeWriter();
        expect(writer.getContent()).andReturn(expected);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
