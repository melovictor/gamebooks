package hu.zagor.gamebooks.books.contentransforming.section;

import hu.zagor.gamebooks.exception.GamebookException;

import org.w3c.dom.Document;

/**
 * Marks that a problem has occurred during the parsing of the {@link Document}.
 * @author Tamas_Szekeres
 *
 */
public class XmlTransformationException extends GamebookException {
    /**
     * Basic constructor with a message.
     * @param message a message describing the problem
     */
    public XmlTransformationException(final String message) {
        super(message);
    }
}
