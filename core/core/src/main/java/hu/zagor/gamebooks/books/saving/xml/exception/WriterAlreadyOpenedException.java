package hu.zagor.gamebooks.books.saving.xml.exception;

import hu.zagor.gamebooks.books.saving.xml.XmlNodeWriter;

/**
 * Exception marking that the {@link XmlNodeWriter} has already been opened and a second opening is not
 * possible.
 * @author Tamas_Szekeres
 */
public class WriterAlreadyOpenedException extends RuntimeException {

}
