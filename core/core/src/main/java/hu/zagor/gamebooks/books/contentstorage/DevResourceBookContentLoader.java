package hu.zagor.gamebooks.books.contentstorage;

import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.io.XmlParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * Implementation of {@link BookContentLoader} interface that loads the contents of the file from a classpath resource, and can load content data from the helper file as well.
 * @author Tamas_Szekeres
 */
public class DevResourceBookContentLoader extends ResourceBookContentLoader {
    /**
     * Basic constructor that supplies the {@link XmlParser} bean to use for parsing the content.
     * @param xmlParser the parser, cannot be null
     */
    public DevResourceBookContentLoader(final XmlParser xmlParser) {
        super(xmlParser);
    }

    @Override
    Map<String, Paragraph> loadParagraphs(final BookInformations info) throws IOException, XmlTransformationException {
        final String paragraphLocation = info.getContents().getParagraphs();
        final Map<String, Paragraph> paragraphs = super.loadParagraphs(info);
        final Resource[] resources = getApplicationContext().getResources(CLASSPATH + paragraphLocation.replace(".xml", "2.xml"));
        if (resources.length > 0) {
            try (InputStream inputStream = resources[0].getInputStream()) {
                final Document xmlFileContent = getXmlParser().getXmlFileContent(inputStream);
                paragraphs.putAll(info.getContentTransformers().getParagraphTransformer().transformParagraphs(xmlFileContent));
            }
        }
        return paragraphs;
    }
}
