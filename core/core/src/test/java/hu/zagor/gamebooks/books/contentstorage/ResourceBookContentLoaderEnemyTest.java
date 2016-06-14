package hu.zagor.gamebooks.books.contentstorage;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.books.contentransforming.enemy.BookEnemyTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.books.contentstorage.domain.BookEntryStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.domain.BookContentFiles;
import hu.zagor.gamebooks.domain.BookContentTransformers;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.io.XmlParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * Unit test for class {@link ResourceBookContentLoader}.
 * @author Tamas_Szekeres
 */
@Test
public class ResourceBookContentLoaderEnemyTest {

    private static final String TITLE = "book title";
    private static final Long ID = 5465465486415345L;
    private static final String PARAGRAPH_PATH = "content.xml";
    private static final String ENEMY_PATH = "enemy.xml";
    private ResourceBookContentLoader underTest;
    private IMocksControl mockControl;
    private XmlParser xmlParser;
    private ApplicationContext applicationContext;
    private Logger logger;
    private BookInformations info;
    private InputStream inputStream;
    private BookEntryStorage storage;
    private BookContentFiles contentFiles;
    private Resource[] resources;
    private Resource resource;
    private Document document;
    private BookContentTransformers contentTransformers;
    private BookParagraphTransformer paragraphTransformer;
    private Map<String, Paragraph> paragraphs;
    private BookEnemyTransformer enemyTransformer;
    private Map<String, Enemy> enemies;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();

        xmlParser = mockControl.createMock(XmlParser.class);
        applicationContext = mockControl.createMock(ApplicationContext.class);
        logger = mockControl.createMock(Logger.class);
        inputStream = mockControl.createMock(InputStream.class);
        storage = mockControl.createMock(BookEntryStorage.class);
        resource = mockControl.createMock(Resource.class);
        resources = new Resource[]{resource};
        document = mockControl.createMock(Document.class);
        paragraphTransformer = mockControl.createMock(BookParagraphTransformer.class);
        paragraphs = new HashMap<>();
        enemies = new HashMap<>();
        enemyTransformer = mockControl.createMock(BookEnemyTransformer.class);

        contentFiles = new BookContentFiles(PARAGRAPH_PATH, ENEMY_PATH, null);
        contentTransformers = new BookContentTransformers(paragraphTransformer, null, enemyTransformer);

        info = new BookInformations(ID);
        info.setTitle(TITLE);
        info.setContents(contentFiles);
        info.setContentTransformers(contentTransformers);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ResourceBookContentLoader(xmlParser);
        underTest.setApplicationContext(applicationContext);
        Whitebox.setInternalState(underTest, "logger", logger);
        mockControl.reset();
    }

    public void testLoadBookContentWhenItemLocationIsNotNullShouldReadParagraphsAndItems() throws XmlTransformationException, IOException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ENEMY_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(enemyTransformer.transformEnemies(document)).andReturn(enemies);
        inputStream.close();

        expect(applicationContext.getBean("bookEntryStorage", ID, paragraphs, null, enemies)).andReturn(storage);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertSame(returned, storage);
    }

    public void testLoadBookContentWhenItemFileIsMissingShouldLogErrorAndReturnNull() throws IOException, XmlTransformationException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ENEMY_PATH)).andReturn(new Resource[0]);

        logger.error(eq("Failed to load contents for book '{}'!"), eq(ID), anyObject(ArrayIndexOutOfBoundsException.class));

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadBookContentWhenInputStreamCannotBeOpenedShouldLogErrorAndReturnNull() throws IOException, XmlTransformationException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ENEMY_PATH)).andReturn(resources);
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
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ENEMY_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        final XmlTransformationException exception = new XmlTransformationException(TITLE);
        expect(enemyTransformer.transformEnemies(document)).andThrow(exception);
        inputStream.close();

        logger.error("Failed to load contents for book '{}'!", ID, exception);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadBookContentWhenInputStreamCannotBeTransformedAndClosingFailsShouldLogErrorAndReturnNull() throws XmlTransformationException,
        IOException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ENEMY_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        final XmlTransformationException exception = new XmlTransformationException(TITLE);
        expect(enemyTransformer.transformEnemies(document)).andThrow(exception);
        inputStream.close();
        expectLastCall().andThrow(new IOException());

        logger.error("Failed to load contents for book '{}'!", ID, exception);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
