package hu.zagor.gamebooks.books.contentstorage;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentransforming.enemy.BookEnemyTransformer;
import hu.zagor.gamebooks.books.contentransforming.item.BookItemTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.books.contentstorage.domain.BookEntryStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.character.item.Item;
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
public class ResourceBookContentLoaderNonsenseTest {

    private static final String TITLE = "book title";
    private static final Long ID = 5465465486415345L;
    private static final String PARAGRAPH_PATH = "content.xml";
    private static final String ITEM_PATH = "item.xml";
    private static final String ENEMY_PATH = "enemy.xml";
    private ResourceBookContentLoader underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private XmlParser xmlParser;
    @Inject private ApplicationContext applicationContext;
    @Inject private Logger logger;
    private BookInformations info;
    @Mock private InputStream inputStream;
    @Mock private BookEntryStorage storage;
    private BookContentFiles contentFilesP;
    private BookContentFiles contentFilesPi;
    private BookContentFiles contentFilesPie;
    private Resource[] resources;
    @Mock private Resource resource;
    @Mock private Document document;
    private BookContentTransformers contentTransformers;
    @Mock private BookParagraphTransformer paragraphTransformer;
    @Mock private Map<String, Paragraph> paragraphs;
    @Mock private BookItemTransformer itemTransformer;
    @Mock private BookEnemyTransformer enemyTransformer;
    @Mock private Map<String, Item> items;

    @BeforeClass
    public void setUpClass() {
        resources = new Resource[]{resource};

        contentFilesP = new BookContentFiles(PARAGRAPH_PATH, null, null);
        contentFilesPi = new BookContentFiles(PARAGRAPH_PATH, null, ITEM_PATH);
        contentFilesPie = new BookContentFiles(PARAGRAPH_PATH, ENEMY_PATH, ITEM_PATH);
        contentTransformers = new BookContentTransformers(paragraphTransformer, itemTransformer, enemyTransformer);

        info = new BookInformations(ID);
        info.setTitle(TITLE);
        info.setContentTransformers(contentTransformers);
    }

    @UnderTest
    public ResourceBookContentLoader underTest() {
        return new ResourceBookContentLoader(xmlParser);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testLoadBookContentWhenParagraphInputStreamIsNullAndParserFailsShouldLogErrorAndReturnNull() throws IOException {
        // GIVEN
        info.setContents(contentFilesPi);
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);

        expect(resource.getInputStream()).andReturn(null);

        final IllegalArgumentException exception = new IllegalArgumentException();
        expect(xmlParser.getXmlFileContent(null)).andThrow(exception);

        logger.error("Failed to load contents for book '{}'!", ID, exception);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadBookContentWhenParagraphInputStreamIsNullAndParserDoesntFailShouldLogErrorAndReturnNull() throws XmlTransformationException, IOException {
        // GIVEN
        info.setContents(contentFilesP);

        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);

        expect(resource.getInputStream()).andReturn(null);

        expect(xmlParser.getXmlFileContent(null)).andReturn(null);
        expect(paragraphTransformer.transformParagraphs(null)).andReturn(null);

        expect(applicationContext.getBean("bookEntryStorage", ID, null, null, null)).andReturn(storage);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertSame(returned, storage);
    }

    public void testLoadBookContentWhenItemInputStreamIsNullAndParserFailsShouldLogErrorAndReturnNull() throws XmlTransformationException, IOException {
        // GIVEN
        info.setContents(contentFilesPi);
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ITEM_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(null);

        final IllegalArgumentException exception = new IllegalArgumentException();
        expect(xmlParser.getXmlFileContent(null)).andThrow(exception);

        logger.error("Failed to load contents for book '{}'!", ID, exception);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadBookContentWhenItemInputStreamIsNullAndParserDoesntFailShouldLogErrorAndReturnNull() throws XmlTransformationException, IOException {
        // GIVEN
        info.setContents(contentFilesPi);
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ITEM_PATH)).andReturn(resources);

        expect(resource.getInputStream()).andReturn(null);

        expect(xmlParser.getXmlFileContent(null)).andReturn(null);
        expect(itemTransformer.transformItems(null)).andReturn(null);

        expect(applicationContext.getBean("bookEntryStorage", ID, paragraphs, null, null)).andReturn(storage);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertSame(returned, storage);
    }

    public void testLoadBookContentWhenEnemyInputStreamIsNullAndParserFailsShouldLogErrorAndReturnNull() throws XmlTransformationException, IOException {
        // GIVEN
        info.setContents(contentFilesPie);
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ITEM_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(itemTransformer.transformItems(document)).andReturn(items);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ENEMY_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(null);

        final IllegalArgumentException exception = new IllegalArgumentException();
        expect(xmlParser.getXmlFileContent(null)).andThrow(exception);

        logger.error("Failed to load contents for book '{}'!", ID, exception);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertNull(returned);
    }

    public void testLoadBookContentWhenEnemyInputStreamIsNullAndParserDoesntFailShouldLogErrorAndReturnNull() throws XmlTransformationException, IOException {
        // GIVEN
        info.setContents(contentFilesPie);
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ITEM_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(itemTransformer.transformItems(document)).andReturn(items);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + ENEMY_PATH)).andReturn(resources);

        expect(resource.getInputStream()).andReturn(null);

        expect(xmlParser.getXmlFileContent(null)).andReturn(null);
        expect(enemyTransformer.transformEnemies(null)).andReturn(null);

        expect(applicationContext.getBean("bookEntryStorage", ID, paragraphs, items, null)).andReturn(storage);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertSame(returned, storage);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
