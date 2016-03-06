package hu.zagor.gamebooks.books.saving.xml;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterAlreadyClosedException;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterAlreadyOpenedException;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterNotYetClosedException;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterNotYetOpenedException;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.StartDocument;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultXmlNodeWriter}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultXmlNodeWriterNegativeTest {

    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String TYPE = "java.lang.String";

    private DefaultXmlNodeWriter underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private XMLOutputFactory outputFactory;
    @Mock private XMLEventFactory eventFactory;
    @Mock private XMLEventWriter eventWriter;
    @Mock private StartDocument startDocument;
    @Mock private EndDocument endDocument;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultXmlNodeWriter(outputFactory, eventFactory);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenOutputFactoryIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new DefaultXmlNodeWriter(null, eventFactory).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenEventFactoryIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new DefaultXmlNodeWriter(outputFactory, null).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterAlreadyOpenedException.class)
    public void testOpenWriterWhenHasBeenOpenedShouldThrowException() throws XMLStreamException {
        expectDocStart();
        mockControl.replay();

        underTest.openWriter();
        // WHEN
        underTest.openWriter();
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterNotYetOpenedException.class)
    public void testCloseWriterWhenWriterWasNotOpenedShouldThrowException() throws XMLStreamException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.closeWriter();
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterAlreadyClosedException.class)
    public void testCloseWriterWhenWriterWasClosedShouldThrowException() throws XMLStreamException {
        expectDocStart();
        expectDocEnd();
        mockControl.replay();

        underTest.openWriter();
        underTest.closeWriter();
        // WHEN
        underTest.closeWriter();
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterNotYetOpenedException.class)
    public void testCloseWriterWhenWriterHasNotBeenOpenedShouldThrowException() throws XMLStreamException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.closeWriter();
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterNotYetOpenedException.class)
    public void testOpenNodeWhenWriterWasNotOpenedShouldThrowException() throws XMLStreamException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.openNode(NAME);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterAlreadyClosedException.class)
    public void testOpenNodeWhenWriterWasClosedShouldThrowException() throws XMLStreamException {
        expectDocStart();
        expectDocEnd();
        mockControl.replay();

        underTest.openWriter();
        underTest.closeWriter();
        // WHEN
        underTest.openNode(NAME);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterAlreadyClosedException.class)
    public void testCloseNodeWhenWriterWasClosedShouldThrowException() throws XMLStreamException {
        expectDocStart();
        expectDocEnd();
        mockControl.replay();

        underTest.openWriter();
        underTest.closeWriter();
        // WHEN
        underTest.closeNode(NAME);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterNotYetOpenedException.class)
    public void testCloseNodeWhenWriterWasNotOpenedShouldThrowException() throws XMLStreamException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.closeNode(NAME);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterAlreadyClosedException.class)
    public void testAddAttributeWhenWriterWasClosedShouldThrowException() throws XMLStreamException {
        expectDocStart();
        expectDocEnd();
        mockControl.replay();

        underTest.openWriter();
        underTest.closeWriter();
        // WHEN
        underTest.addAttribute(NAME, VALUE);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterNotYetOpenedException.class)
    public void testAddAttributeWhenWriterWasNotOpenedShouldThrowException() throws XMLStreamException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addAttribute(NAME, VALUE);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterAlreadyClosedException.class)
    public void testCreateSimpleNodeWhenWriterWasClosedShouldThrowException() throws XMLStreamException {
        expectDocStart();
        expectDocEnd();
        mockControl.replay();

        underTest.openWriter();
        underTest.closeWriter();
        // WHEN
        underTest.createSimpleNode(NAME, VALUE, TYPE);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterNotYetOpenedException.class)
    public void testCreateSimpleNodeWhenWriterWasNotOpenedShouldThrowException() throws XMLStreamException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.createSimpleNode(NAME, VALUE, TYPE);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterAlreadyClosedException.class)
    public void testCreateSimpleNodeWhenWriterWasClosedAndNodeIsNullShouldThrowException() throws XMLStreamException {
        expectDocStart();
        expectDocEnd();
        mockControl.replay();

        underTest.openWriter();
        underTest.closeWriter();
        // WHEN
        underTest.createSimpleNode(NAME);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterNotYetOpenedException.class)
    public void testCreateSimpleNodeWhenWriterWasNotOpenedAndNodeIsNullShouldThrowException() throws XMLStreamException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.createSimpleNode(NAME);
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterNotYetOpenedException.class)
    public void testGetContentWhenWriterHasNotBeenOpenedShouldThrowException() throws UnsupportedEncodingException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getContent();
        // THEN throws exception
    }

    @Test(expectedExceptions = WriterNotYetClosedException.class)
    public void testGetContentWhenWriterHasNotBeenClosedShouldThrowException() throws UnsupportedEncodingException, XMLStreamException {
        // GIVEN
        expectDocStart();
        mockControl.replay();

        underTest.openWriter();
        // WHEN
        underTest.getContent();
        // THEN throws exception
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

}
