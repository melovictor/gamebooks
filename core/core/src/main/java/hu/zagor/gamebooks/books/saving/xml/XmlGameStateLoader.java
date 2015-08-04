package hu.zagor.gamebooks.books.saving.xml;


/**
 * Helper interface for loading a serializable object from xml.
 * @author Tamas_Szekeres
 */
public interface XmlGameStateLoader {

    /**
     * Generates an object from the input xml string.
     * @param content the content to parse
     * @return the deserialized object or null if an error occured during deserialization
     */
    Object load(String content);

}
