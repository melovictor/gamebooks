package hu.zagor.gamebooks.books.saving.xml;


/**
 * Helper interface for saving a serializable object into xml.
 * @author Tamas_Szekeres
 */
public interface XmlGameStateSaver {

    /**
     * Generates an xml string from the input object.
     * @param object the object to serialize
     * @return the serialized xml or null if an error occured during serialization
     */
    String save(Object object);

}
