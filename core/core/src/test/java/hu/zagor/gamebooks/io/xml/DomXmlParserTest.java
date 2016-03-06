package hu.zagor.gamebooks.io.xml;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Unit test for class {@link DomXmlParser}.
 * @author Tamas_Szekeres
 */
@Test
public class DomXmlParserTest {

    @UnderTest private DomXmlParser underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private DocumentBuilderFactory builderFactory;
    @Mock private DocumentBuilder documentBuilder;
    @Mock private Document document;
    @Mock private InputStream inputStream;
    @Inject private Logger logger;

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetXmlFileContentWhenInputStreamIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getXmlFileContent(null);
        // THEN throws exception
    }

    public void testGetXmlFileContentWhenParsingThrowsSaxExceptionShouldLogAndReturnNull() throws SAXException, IOException, ParserConfigurationException {
        // GIVEN
        expect(builderFactory.newDocumentBuilder()).andReturn(documentBuilder);
        final Exception exception = new SAXException();
        expect(documentBuilder.parse(inputStream)).andThrow(exception);
        logger.error("Failed to parse the content of the input stream!", exception);
        mockControl.replay();
        // WHEN
        final Document returned = underTest.getXmlFileContent(inputStream);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetXmlFileContentWhenParsingThrowsIoExceptionShouldLogAndReturnNull() throws SAXException, IOException, ParserConfigurationException {
        // GIVEN
        expect(builderFactory.newDocumentBuilder()).andReturn(documentBuilder);
        final Exception exception = new IOException();
        expect(documentBuilder.parse(inputStream)).andThrow(exception);
        logger.error("Failed to read the content of the input stream!", exception);
        mockControl.replay();
        // WHEN
        final Document returned = underTest.getXmlFileContent(inputStream);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetXmlFileContentWhenParsingSucceedsShouldReturnParsedDocument() throws SAXException, IOException, ParserConfigurationException {
        // GIVEN
        expect(builderFactory.newDocumentBuilder()).andReturn(documentBuilder);
        expect(documentBuilder.parse(inputStream)).andReturn(document);
        mockControl.replay();
        // WHEN
        final Document returned = underTest.getXmlFileContent(inputStream);
        // THEN
        Assert.assertSame(returned, document);
    }

    public void testGetXmlFileContentWhenDocumentBuilderCreationThrowsParserConfigurationExceptionShouldLogAndReturnNull() throws ParserConfigurationException {
        // GIVEN
        final Exception exception = new ParserConfigurationException();
        expect(builderFactory.newDocumentBuilder()).andThrow(exception);
        logger.error("Failed to create the parser object!", exception);
        mockControl.replay();
        // WHEN
        final Document returned = underTest.getXmlFileContent(inputStream);
        // THEN
        Assert.assertNull(returned);
    }

}
