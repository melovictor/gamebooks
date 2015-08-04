package hu.zagor.gamebooks.books.saving.xml.exception;

import hu.zagor.gamebooks.books.saving.xml.XmlNodeWriter;

/**
 * Exception marking that the {@link XmlNodeWriter} has already been closed and no more method call (opening,
 * closing, writing) is possible except for obtaining the reference stream.
 * @author Tamas_Szekeres
 */
public class WriterAlreadyClosedException extends RuntimeException {

}
