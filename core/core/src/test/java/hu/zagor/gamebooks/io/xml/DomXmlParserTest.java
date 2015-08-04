package hu.zagor.gamebooks.io.xml;

import static org.easymock.EasyMock.expect;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Unit test for class {@link DomXmlParser}.
 * @author Tamas_Szekeres
 */
@Test
@PrepareForTest(DocumentBuilderFactory.class)
public class DomXmlParserTest {

    private DomXmlParser underTest;
    private IMocksControl mockControl;

    private DocumentBuilderFactory builderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private InputStream inputStream;
    private Logger logger;

    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new PowerMockObjectFactory();
    }

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        builderFactory = mockControl.createMock(DocumentBuilderFactory.class);
        documentBuilder = mockControl.createMock(DocumentBuilder.class);
        logger = mockControl.createMock(Logger.class);
        document = mockControl.createMock(Document.class);
        inputStream = mockControl.createMock(InputStream.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DomXmlParser();
        Whitebox.setInternalState(underTest, "logger", logger);
        PowerMock.mockStatic(DocumentBuilderFactory.class);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetXmlFileContentWhenInputStreamIsNullShouldThrowException() {
        // GIVEN
        PowerMock.replay(DocumentBuilderFactory.class);
        mockControl.replay();
        // WHEN
        underTest.getXmlFileContent(null);
        // THEN throws exception
    }

    public void testGetXmlFileContentWhenParsingThrowsSaxExceptionShouldLogAndReturnNull() throws SAXException, IOException,
        ParserConfigurationException {
        // GIVEN
        expect(DocumentBuilderFactory.newInstance()).andReturn(builderFactory);
        expect(builderFactory.newDocumentBuilder()).andReturn(documentBuilder);
        final Exception exception = new SAXException();
        expect(documentBuilder.parse(inputStream)).andThrow(exception);
        logger.error("Failed to parse the content of the input stream!", exception);
        PowerMock.replay(DocumentBuilderFactory.class);
        mockControl.replay();
        // WHEN
        final Document returned = underTest.getXmlFileContent(inputStream);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetXmlFileContentWhenParsingThrowsIoExceptionShouldLogAndReturnNull() throws SAXException, IOException,
        ParserConfigurationException {
        // GIVEN
        expect(DocumentBuilderFactory.newInstance()).andReturn(builderFactory);
        expect(builderFactory.newDocumentBuilder()).andReturn(documentBuilder);
        final Exception exception = new IOException();
        expect(documentBuilder.parse(inputStream)).andThrow(exception);
        logger.error("Failed to read the content of the input stream!", exception);
        PowerMock.replay(DocumentBuilderFactory.class);
        mockControl.replay();
        // WHEN
        final Document returned = underTest.getXmlFileContent(inputStream);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetXmlFileContentWhenParsingSucceedsShouldReturnParsedDocument() throws SAXException, IOException, ParserConfigurationException {
        // GIVEN
        expect(DocumentBuilderFactory.newInstance()).andReturn(builderFactory);
        expect(builderFactory.newDocumentBuilder()).andReturn(documentBuilder);
        expect(documentBuilder.parse(inputStream)).andReturn(document);
        PowerMock.replay(DocumentBuilderFactory.class);
        mockControl.replay();
        // WHEN
        final Document returned = underTest.getXmlFileContent(inputStream);
        // THEN
        Assert.assertSame(returned, document);
    }

    public void testGetXmlFileContentWhenDocumentBuilderCreationThrowsParserConfigurationExceptionShouldLogAndReturnNull()
        throws ParserConfigurationException {
        // GIVEN
        expect(DocumentBuilderFactory.newInstance()).andReturn(builderFactory);
        final Exception exception = new ParserConfigurationException();
        expect(builderFactory.newDocumentBuilder()).andThrow(exception);
        logger.error("Failed to create the parser object!", exception);
        PowerMock.replay(DocumentBuilderFactory.class);
        mockControl.replay();
        // WHEN
        final Document returned = underTest.getXmlFileContent(inputStream);
        // THEN
        Assert.assertNull(returned);
    }

    @AfterMethod
    public void tearDownMethod() {
        PowerMock.verify(DocumentBuilderFactory.class);
        mockControl.verify();
    }

}
