package hu.zagor.gamebooks.books.contentstorage;

import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.io.XmlParser;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.w3c.dom.Document;

/**
 * Implementation of {@link ApplicationContext} interface that loads the contents of the file from a classpath
 * resource.
 * @author Tamas_Szekeres
 */
public class ResourceBookContentLoader implements BookContentLoader, ApplicationContextAware {

    private static final String CLASSPATH = "classpath*:/";
    @LogInject
    private Logger logger;
    private ApplicationContext applicationContext;
    private final XmlParser xmlParser;

    /**
     * Basic constructor that supplies the {@link XmlParser} bean to use for parsing the content.
     * @param xmlParser the parser, cannot be null
     */
    public ResourceBookContentLoader(final XmlParser xmlParser) {
        Assert.notNull(xmlParser, "Parameter 'xmlParser' can not be null!");
        this.xmlParser = xmlParser;
    }

    @Override
    public BookItemStorage loadBookContent(final BookInformations info) {
        Assert.notNull(info, "Parameter 'info' can not be null!");
        BookItemStorage storage = null;
        try {
            logger.info("Loading content for book '{}'.", info.getTitle());

            final Map<String, Paragraph> paragraphs = loadParagraphs(info);
            final Map<String, Item> items = loadItems(info);
            final Map<String, Enemy> enemies = loadEnemies(info);

            storage = (BookItemStorage) applicationContext.getBean("bookEntryStorage", info.getId(), paragraphs, items, enemies);
        } catch (final IOException | XmlTransformationException | IndexOutOfBoundsException | IllegalArgumentException ex) {
            logger.error("Failed to load contents for book '{}'!", info.getId(), ex);
        }
        return storage;
    }

    private Map<String, Paragraph> loadParagraphs(final BookInformations info) throws IOException, XmlTransformationException {
        final String paragraphLocation = info.getContents().getParagraphs();
        Map<String, Paragraph> paragraphs = null;
        try (InputStream inputStream = applicationContext.getResources(CLASSPATH + paragraphLocation)[0].getInputStream()) {
            final Document xmlFileContent = xmlParser.getXmlFileContent(inputStream);
            paragraphs = info.getContentTransformers().getParagraphTransformer().transformParagraphs(xmlFileContent);
        }
        return paragraphs;
    }

    private Map<String, Item> loadItems(final BookInformations info) throws IOException, XmlTransformationException {
        final String itemsLocation = info.getContents().getItems();
        Map<String, Item> items = null;
        if (itemsLocation != null && itemsLocation.length() > 0) {
            try (InputStream inputStream = applicationContext.getResources(CLASSPATH + itemsLocation)[0].getInputStream()) {
                final Document xmlFileContent = xmlParser.getXmlFileContent(inputStream);
                items = info.getContentTransformers().getItemTransformer().transformItems(xmlFileContent);
            }

        }
        return items;
    }

    private Map<String, Enemy> loadEnemies(final BookInformations info) throws IOException, XmlTransformationException {
        final String enemiesLocation = info.getContents().getEnemies();
        Map<String, Enemy> enemies = null;
        if (enemiesLocation != null && enemiesLocation.length() > 0) {
            try (InputStream inputStream = applicationContext.getResources(CLASSPATH + enemiesLocation)[0].getInputStream()) {
                final Document xmlFileContent = xmlParser.getXmlFileContent(inputStream);
                enemies = info.getContentTransformers().getEnemyTransformer().transformEnemies(xmlFileContent);
            }
        }
        return enemies;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
