package hu.zagor.gamebooks.io;

import java.io.InputStream;

import org.w3c.dom.Document;

/**
 * Interface for reading the contents of an xml file into memory.
 * @author Tamas_Szekeres
 *
 */
public interface XmlParser {
    /**
     * Reads the contents of an xml input stream.
     * @param inputStream the input stream containing the data, cannot be null
     * @return the parsed object or null, if there was some problem during the parsing
     */
    Document getXmlFileContent(InputStream inputStream);

}
