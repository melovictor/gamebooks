package hu.zagor.gamebooks.io.xml;

import hu.zagor.gamebooks.io.XmlParser;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Dom implementation of the {@link XmlParser} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DomXmlParser implements XmlParser {

    @LogInject private Logger logger;
    @Autowired private DocumentBuilderFactory builderFactory;

    @Override
    public Document getXmlFileContent(final InputStream inputStream) {
        Assert.notNull(inputStream, "The parameter 'inputStream' cannot be null!");
        Document document = null;
        try {
            final DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            document = documentBuilder.parse(inputStream);
        } catch (final ParserConfigurationException parserConfigurationException) {
            logger.error("Failed to create the parser object!", parserConfigurationException);
        } catch (final SAXException saxException) {
            logger.error("Failed to parse the content of the input stream!", saxException);
        } catch (final IOException ioException) {
            logger.error("Failed to read the content of the input stream!", ioException);
        }

        return document;
    }

}
