package hu.zagor.gamebooks.books.saving.xml.exception;

import hu.zagor.gamebooks.books.saving.xml.XmlNodeWriter;

/**
 * Exception marking that the {@link XmlNodeWriter} has not yet been closed and so getting the reference
 * stream is not possible.
 * @author Tamas_Szekeres
 */
public class WriterNotYetClosedException extends RuntimeException {

}
