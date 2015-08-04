package hu.zagor.gamebooks.books.saving.xml;

import hu.zagor.gamebooks.books.saving.xml.exception.WriterAlreadyClosedException;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterAlreadyOpenedException;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterNotYetClosedException;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterNotYetOpenedException;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

import org.springframework.util.Assert;

/**
 * Default implementation of the {@link XmlNodeWriter} interface.
 * @author Tamas_Szekeres
 */
public class DefaultXmlNodeWriter implements XmlNodeWriter {

    private static final String UTF_8 = "UTF-8";
    private final XMLEventFactory eventFactory;
    private final XMLOutputFactory outputFactory;

    private XMLEventWriter eventWriter;
    private ByteArrayOutputStream stream;

    private boolean isOpened;
    private boolean isClosed;

    /**
     * Basic constructor.
     * @param outputFactory an instance of an {@link XMLOutputFactory} to use for the xml file generation,
     * cannot be null
     * @param eventFactory an instance of an {@link XMLEventFactory} to use for the xml file generation,
     * cannot be null
     */
    public DefaultXmlNodeWriter(final XMLOutputFactory outputFactory, final XMLEventFactory eventFactory) {
        Assert.notNull(outputFactory, "The parameter 'outputFactory' cannot be null!");
        Assert.notNull(eventFactory, "The parameter 'eventFactory' cannot be null!");

        this.eventFactory = eventFactory;
        this.outputFactory = outputFactory;
    }

    @Override
    public void openWriter() throws XMLStreamException {
        checkForOpening();
        stream = new ByteArrayOutputStream();
        eventWriter = outputFactory.createXMLEventWriter(stream, UTF_8);

        final StartDocument startDocument = eventFactory.createStartDocument(UTF_8, "1.0");
        eventWriter.add(startDocument);
    }

    @Override
    public void closeWriter() throws XMLStreamException {
        checkForClosing();
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }

    @Override
    public void openNode(final String name) throws XMLStreamException {
        checkForAlteration();
        final StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(sElement);
    }

    @Override
    public void addAttribute(final String name, final String value) throws XMLStreamException {
        checkForAlteration();
        final Attribute attribute = eventFactory.createAttribute(name, value);
        eventWriter.add(attribute);
    }

    @Override
    public void closeNode(final String name) throws XMLStreamException {
        checkForAlteration();
        final EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
    }

    @Override
    public void createSimpleNode(final String name, final String value, final String type) throws XMLStreamException {
        checkForAlteration();
        openNode(name);
        addAttribute("class", type);
        final Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        closeNode(name);
    }

    @Override
    public void createSimpleNode(final String name) throws XMLStreamException {
        checkForAlteration();
        openNode(name);
        addAttribute("isNull", "true");
        closeNode(name);
    }

    @Override
    public String getContent() throws UnsupportedEncodingException {
        checkForClosed();
        return stream.toString("utf-8");
    }

    private void checkForClosed() {
        mustBeOpened();
        mustBeClosed();
    }

    private void checkForOpening() {
        mustNotBeOpened();
        isOpened = true;
    }

    private void checkForClosing() {
        checkForAlteration();
        isClosed = true;
    }

    private void checkForAlteration() {
        mustBeOpened();
        mustNotBeClosed();
    }

    private void mustBeOpened() {
        if (!isOpened) {
            throw new WriterNotYetOpenedException();
        }
    }

    private void mustNotBeOpened() {
        if (isOpened) {
            throw new WriterAlreadyOpenedException();
        }
    }

    private void mustBeClosed() {
        if (!isClosed) {
            throw new WriterNotYetClosedException();
        }
    }

    private void mustNotBeClosed() {
        if (isClosed) {
            throw new WriterAlreadyClosedException();
        }
    }

}
