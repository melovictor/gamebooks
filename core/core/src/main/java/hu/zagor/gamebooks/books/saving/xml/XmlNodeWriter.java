package hu.zagor.gamebooks.books.saving.xml;

import hu.zagor.gamebooks.books.saving.xml.exception.WriterAlreadyClosedException;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterAlreadyOpenedException;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterNotYetClosedException;
import hu.zagor.gamebooks.books.saving.xml.exception.WriterNotYetOpenedException;

import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLStreamException;

/**
 * A helper interface for generating common parts of the xml document.
 * @author Tamas_Szekeres
 */
public interface XmlNodeWriter {

    /**
     * Method to initialize writer, must be called before any other actions.
     * @throws XMLStreamException when an error occurs during xml writing
     * @throws WriterAlreadyOpenedException when it is called a second time on a writer
     */
    void openWriter() throws XMLStreamException;

    /**
     * Creates a simple node representing a field with a null value.
     * @param name the name of the node
     * @throws XMLStreamException when an error occurs during xml writing
     * @throws WriterNotYetOpenedException when the method is called before the writer has been opened
     * @throws WriterAlreadyClosedException when the method is called after the writer has been closed
     */
    void createSimpleNode(String name) throws XMLStreamException;

    /**
     * Creates a simple node with the given name and value, also recording the type of the field for a simple
     * type (int, string, etc.).
     * @param name the name of the node
     * @param value the value of the node
     * @param type the type of the field represented by the node
     * @throws XMLStreamException when an error occurs during xml writing
     * @throws WriterNotYetOpenedException when the method is called before the writer has been opened
     * @throws WriterAlreadyClosedException when the method is called after the writer has been closed
     */
    void createSimpleNode(String name, String value, String type) throws XMLStreamException;

    /**
     * Opens a new node with the given name for potentially complex objects.
     * @param name the name of the node
     * @throws XMLStreamException when an error occurs during xml writing
     * @throws WriterNotYetOpenedException when the method is called before the writer has been opened
     * @throws WriterAlreadyClosedException when the method is called after the writer has been closed
     */
    void openNode(String name) throws XMLStreamException;

    /**
     * Generates an attribute with the specified name and value (eg. for marking map or list type).
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @throws XMLStreamException when an error occurs during xml writing
     * @throws WriterNotYetOpenedException when the method is called before the writer has been opened
     * @throws WriterAlreadyClosedException when the method is called after the writer has been closed
     */
    void addAttribute(String name, String value) throws XMLStreamException;

    /**
     * Generates the closing tag of the node.
     * @param name the name of the node
     * @throws XMLStreamException when an error occurs during xml writing
     * @throws WriterNotYetOpenedException when the method is called before the writer has been opened
     * @throws WriterAlreadyClosedException when the method is called after the writer has been closed
     */
    void closeNode(String name) throws XMLStreamException;

    /**
     * Closes the xml node writer. After this call no more operation should be executed on this object except
     * for obtaining the content stream.
     * @throws XMLStreamException when an error occurs during xml writing
     * @throws WriterNotYetOpenedException when the method is called before the writer has been opened
     * @throws WriterAlreadyClosedException when the method is called a second time on a writers
     */
    void closeWriter() throws XMLStreamException;

    /**
     * Gets the generated content as UTF-8. Should only be called after the writer has been closed.
     * @return the generated content
     * @throws UnsupportedEncodingException when the UTF-8 encoding is not supported on the current system
     * @throws WriterNotYetOpenedException when the method is called before the writer has been opened
     * @throws WriterNotYetClosedException when the method is called before the writer has been closed
     */
    String getContent() throws UnsupportedEncodingException;

}
