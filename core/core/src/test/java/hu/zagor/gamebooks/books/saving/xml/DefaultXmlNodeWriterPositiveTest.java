package hu.zagor.gamebooks.books.saving.xml;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultXmlNodeWriter}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultXmlNodeWriterPositiveTest {

    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String TYPE = "java.lang.String";

    private DefaultXmlNodeWriter underTest;
    private IMocksControl mockControl;
    private XMLOutputFactory outputFactory;
    private XMLEventFactory eventFactory;
    private XMLEventWriter eventWriter;
    private StartDocument startDocument;
    private EndDocument endDocument;
    private StartElement startElement;
    private EndElement endElement;
    private Attribute attribute;
    private Characters characters;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        outputFactory = mockControl.createMock(XMLOutputFactory.class);
        eventFactory = mockControl.createMock(XMLEventFactory.class);
        eventWriter = mockControl.createMock(XMLEventWriter.class);
        startDocument = mockControl.createMock(StartDocument.class);
        endDocument = mockControl.createMock(EndDocument.class);
        startElement = mockControl.createMock(StartElement.class);
        endElement = mockControl.createMock(EndElement.class);
        attribute = mockControl.createMock(Attribute.class);
        characters = mockControl.createMock(Characters.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultXmlNodeWriter(outputFactory, eventFactory);
        mockControl.reset();
    }

    public void testOpenWriterWhenHasntBeenOpenedShouldRunSuccessfully() throws XMLStreamException {
        expectDocStart();
        mockControl.replay();
        // WHEN
        underTest.openWriter();
        // THEN
    }

    public void testCloseWriterWhenWriterWasOpenedShouldRunSuccessfully() throws XMLStreamException {
        expectDocStart();
        expectDocEnd();
        mockControl.replay();

        underTest.openWriter();
        // WHEN
        underTest.closeWriter();
        // THEN
    }

    public void testOpenNodeWhenWriterWasOpenedShouldRunSuccessfully() throws XMLStreamException {
        expectDocStart();
        expect(eventFactory.createStartElement("", "", NAME)).andReturn(startElement);
        eventWriter.add(startElement);
        mockControl.replay();

        underTest.openWriter();
        // WHEN
        underTest.openNode(NAME);
        // THEN
    }

    public void testCloseNodeWhenWriterWasOpenedShouldRunSuccessfully() throws XMLStreamException {
        expectDocStart();
        expect(eventFactory.createEndElement("", "", NAME)).andReturn(endElement);
        eventWriter.add(endElement);
        mockControl.replay();

        underTest.openWriter();
        // WHEN
        underTest.closeNode(NAME);
        // THEN
    }

    public void testAddAttributeWhenWriterWasOpenedShouldRunSuccessfully() throws XMLStreamException {
        expectDocStart();
        expect(eventFactory.createAttribute(NAME, VALUE)).andReturn(attribute);
        eventWriter.add(attribute);
        mockControl.replay();

        underTest.openWriter();
        // WHEN
        underTest.addAttribute(NAME, VALUE);
        // THEN
    }

    public void testCreateSimpleNodeWhenWriterWasOpenedShouldRunSuccessfully() throws XMLStreamException {
        expectDocStart();
        expect(eventFactory.createStartElement("", "", NAME)).andReturn(startElement);
        eventWriter.add(startElement);
        expect(eventFactory.createAttribute("class", TYPE)).andReturn(attribute);
        eventWriter.add(attribute);
        expect(eventFactory.createCharacters(VALUE)).andReturn(characters);
        eventWriter.add(characters);
        expect(eventFactory.createEndElement("", "", NAME)).andReturn(endElement);
        eventWriter.add(endElement);
        mockControl.replay();

        underTest.openWriter();
        // WHEN
        underTest.createSimpleNode(NAME, VALUE, TYPE);
        // THEN
    }

    public void testCreateSimpleNodeWhenWriterWasOpenedAndNodeIsNullShouldRunSuccessfully() throws XMLStreamException {
        expectDocStart();
        expect(eventFactory.createStartElement("", "", NAME)).andReturn(startElement);
        eventWriter.add(startElement);
        expect(eventFactory.createAttribute("isNull", "true")).andReturn(attribute);
        eventWriter.add(attribute);
        expect(eventFactory.createEndElement("", "", NAME)).andReturn(endElement);
        eventWriter.add(endElement);
        mockControl.replay();

        underTest.openWriter();
        // WHEN
        underTest.createSimpleNode(NAME);
        // THEN
    }

    public void testGetContentWhenWriterHasBeenClosedShouldReturnStream() throws UnsupportedEncodingException, XMLStreamException {
        // GIVEN
        expectDocStart();
        expectDocEnd();
        mockControl.replay();

        underTest.openWriter();
        underTest.closeWriter();
        // WHEN
        final String returned = underTest.getContent();
        // THEN
        Assert.assertNotNull(returned);
    }

    private void expectDocStart() throws XMLStreamException {
        expect(outputFactory.createXMLEventWriter(anyObject(ByteArrayOutputStream.class), eq("UTF-8"))).andReturn(eventWriter);
        expect(eventFactory.createStartDocument("UTF-8", "1.0")).andReturn(startDocument);
        eventWriter.add(startDocument);
    }

    private void expectDocEnd() throws XMLStreamException {
        expect(eventFactory.createEndDocument()).andReturn(endDocument);
        eventWriter.add(endDocument);
        eventWriter.close();
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
