package hu.zagor.gamebooks.books.contentstorage;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.books.contentstorage.domain.BookEntryStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.domain.BookContentFiles;
import hu.zagor.gamebooks.domain.BookContentTransformers;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.io.XmlParser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * Unit test for class {@link ResourceBookContentLoader}.
 * @author Tamas_Szekeres
 */
@Test
public class ResourceBookContentLoaderParagraphTest {

    private static final String TITLE = "book title";
    private static final Long ID = 5465465486415345L;
    private static final String PARAGRAPH_PATH = "content.xml";
    private ResourceBookContentLoader underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private XmlParser xmlParser;
    @Inject private ApplicationContext applicationContext;
    @Inject private Logger logger;
    private BookInformations info;
    @Mock private InputStream inputStream;
    @Mock private BookEntryStorage storage;
    private BookContentFiles contentFiles;
    private Resource[] resources;
    @Mock private Resource resource;
    @Mock private Document document;
    private BookContentTransformers contentTransformers;
    @Mock private BookParagraphTransformer paragraphTransformer;
    @Mock private Map<String, Paragraph> paragraphs;

    @BeforeClass
    public void setUpClass() {
        resources = new Resource[]{resource};

        contentFiles = new BookContentFiles(PARAGRAPH_PATH, null, null);
        contentTransformers = new BookContentTransformers(paragraphTransformer, null, null);

        info = new BookInformations(ID);
        info.setTitle(TITLE);
        info.setContents(contentFiles);
        info.setContentTransformers(contentTransformers);
    }

    @UnderTest
    public ResourceBookContentLoader underTest() {
        return new ResourceBookContentLoader(xmlParser);
    }

    public void testLoadBookContentWhenItemLocationIsNullShouldReadParagraphsOnly() throws XmlTransformationException, IOException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getBean("bookEntryStorage", ID, paragraphs, null, null)).andReturn(storage);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertSame(returned, storage);
    }

    public void testLoadBookContentWhenItemLocationIsEmptyShouldReadParagraphsOnly() throws XmlTransformationException, IOException {
        // GIVEN
        info.setContents(new BookContentFiles(PARAGRAPH_PATH, null, ""));

        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getBean("bookEntryStorage", ID, paragraphs, null, null)).andReturn(storage);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertSame(returned, storage);
    }

    public void testLoadBookContentWhenParagraphFileIsMissingShouldLogErrorAndReturnNull() throws IOException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(new Resource[0]);

        logger.error(eq("Failed to load contents for book '{}'!"), eq(ID), anyObject(ArrayIndexOutOfBoundsException.class));

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadBookContentWhenInputStreamCannotBeOpenedShouldLogErrorAndReturnNull() throws IOException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);

        final IOException exception = new IOException();
        expect(resource.getInputStream()).andThrow(exception);

        logger.error("Failed to load contents for book '{}'!", ID, exception);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadBookContentWhenInputStreamCannotBeTransformedShouldLogErrorAndReturnNull() throws XmlTransformationException, IOException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);

        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        final XmlTransformationException exception = new XmlTransformationException(TITLE);
        expect(paragraphTransformer.transformParagraphs(document)).andThrow(exception);
        inputStream.close();

        logger.error("Failed to load contents for book '{}'!", ID, exception);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadBookContentWhenInputStreamCannotBeTransformedAndClosingFailsShouldLogErrorAndReturnNull() throws XmlTransformationException, IOException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);

        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        final XmlTransformationException exception = new XmlTransformationException(TITLE);
        expect(paragraphTransformer.transformParagraphs(document)).andThrow(exception);
        inputStream.close();
        expectLastCall().andThrow(new IOException());

        logger.error("Failed to load contents for book '{}'!", ID, exception);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetXmlParserShouldReturnXmlParser() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final XmlParser returned = underTest.getXmlParser();
        // THEN
        Assert.assertSame(returned, xmlParser);
    }

    public void testGetApplicationContextShouldReturnApplicationContext() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final ApplicationContext returned = underTest.getApplicationContext();
        // THEN
        Assert.assertSame(returned, applicationContext);
    }
}
