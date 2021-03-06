package hu.zagor.gamebooks.books.contentstorage;

import static org.easymock.EasyMock.expect;
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
 * Unit test for class {@link DevResourceBookContentLoader}.
 * @author Tamas_Szekeres
 */
@Test
public class DevResourceBookContentLoaderTest {
    private static final String TITLE = "book title";
    private static final Long ID = 5465465486415345L;
    private static final String PARAGRAPH_PATH = "content.xml";
    private static final String PARAGRAPH_PATH_ALT = "content2.xml";

    @MockControl private IMocksControl mockControl;
    private DevResourceBookContentLoader underTest;
    @Mock private XmlParser xmlParser;
    @Inject private Logger logger;
    @Inject private ApplicationContext applicationContext;
    private Resource[] resources;
    @Mock private Resource resource;
    private BookContentFiles contentFiles;
    private BookContentTransformers contentTransformers;
    @Mock private BookParagraphTransformer paragraphTransformer;
    @Mock private Document document;
    private BookInformations info;
    @Mock private InputStream inputStream;
    @Mock private Map<String, Paragraph> paragraphs;
    @Mock private Map<String, Paragraph> paragraphsAlt;
    @Mock private BookEntryStorage storage;

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
    public DevResourceBookContentLoader underTest() {
        return new DevResourceBookContentLoader(xmlParser);
    }

    public void testLoadParagraphsWhenNoAlternateResourceFoundShouldLoadOnlyFromBasicBatch() throws XmlTransformationException, IOException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH_ALT)).andReturn(new Resource[]{});

        expect(applicationContext.getBean("bookEntryStorage", ID, paragraphs, null, null)).andReturn(storage);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertSame(returned, storage);
    }

    public void testLoadParagraphsWhenAlternateResourceFoundShouldExtendResultsFromBasicBatch() throws XmlTransformationException, IOException {
        // GIVEN
        logger.info("Loading content for book '{}'.", TITLE);

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphs);
        inputStream.close();

        expect(applicationContext.getResources("classpath*:/" + PARAGRAPH_PATH_ALT)).andReturn(resources);
        expect(resource.getInputStream()).andReturn(inputStream);

        expect(xmlParser.getXmlFileContent(inputStream)).andReturn(document);
        expect(paragraphTransformer.transformParagraphs(document)).andReturn(paragraphsAlt);
        inputStream.close();
        paragraphs.putAll(paragraphsAlt);

        expect(applicationContext.getBean("bookEntryStorage", ID, paragraphs, null, null)).andReturn(storage);

        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.loadBookContent(info);
        // THEN
        Assert.assertSame(returned, storage);
    }

}
