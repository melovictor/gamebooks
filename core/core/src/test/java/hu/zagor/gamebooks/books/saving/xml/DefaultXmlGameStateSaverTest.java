package hu.zagor.gamebooks.books.saving.xml;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.startsWith;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithList;
import hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultXmlGameStateSaver}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultXmlGameStateSaverTest {
    @UnderTest private DefaultXmlGameStateSaver underTest;
    @Inject private Logger logger;
    @MockControl private IMocksControl mockControl;
    @Inject private AutowireCapableBeanFactory beanFactory;
    @Mock private DefaultXmlNodeWriter writer;

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
        final String expected = "response";
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
        final String expected = "response";
        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "fieldWithNumber", "java.lang.String");
        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt");
        logger.debug("Saving class hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt");
        writer.addAttribute("ref", "1");
        logger.debug("Saving field 'intField' with value '1534'.");
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

    public void testSaveWhenInputContainsDuplicatedSimpleClassWithIntegerShouldCreateProperXmlOutput() throws UnsupportedEncodingException, XMLStreamException {
        // GIVEN
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        final SimpleClassWithInt value = new SimpleClassWithInt();
        input.put("fieldWithNumber", value);
        input.put("otherFieldWithNumber", value);
        final String expected = "response";
        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "fieldWithNumber", "java.lang.String");
        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt");
        logger.debug("Saving class hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt");
        writer.addAttribute("ref", "1");
        logger.debug("Saving field 'intField' with value '1534'.");
        writer.createSimpleNode("intField", "1534", "java.lang.Integer");
        writer.closeNode("value");
        writer.closeNode("mapEntry");

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "otherFieldWithNumber", "java.lang.String");
        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithInt");
        logger.debug("Saving class reference 1");
        writer.addAttribute("ref", "1");
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
        final String expected = "response";
        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "fieldWithList", "java.lang.String");

        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithList");
        logger.debug("Saving class hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithList");
        writer.addAttribute("ref", "1");
        logger.debug("Saving field 'elements' with value '[apple, pear]'.");

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
        final String expected = "response";

        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "fieldWithEnum", "java.lang.String");

        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum");
        logger.debug("Saving class hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithEnum");
        writer.addAttribute("ref", "1");
        logger.debug("Saving field 'enumField' with value 'KIWI'.");

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
        final String expected = "response";
        expectStartMainObject();

        writer.openNode("mapEntry");
        writer.createSimpleNode("key", "fieldWithNull", "java.lang.String");
        writer.openNode("value");
        writer.addAttribute("class", "hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull");
        logger.debug("Saving class hu.zagor.gamebooks.books.saving.xml.input.SimpleClassWithNull");
        writer.addAttribute("ref", "1");
        logger.debug("Saving field 'nullField' with value 'null'.");
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
        logger.debug("Saving class hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper");
        writer.addAttribute("ref", "0");
        logger.debug(startsWith("Saving field 'element' with value '{"));

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
}
