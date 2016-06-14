package hu.zagor.gamebooks.books.saving.xml.input;

import hu.zagor.gamebooks.books.saving.xml.DefaultXmlGameStateSaver;
import java.io.Serializable;

/**
 * Test class for testing {@link DefaultXmlGameStateSaver}.
 * @author Tamas_Szekeres
 */
public class SimpleClassWithNull implements Serializable {

    private String nullField;

    public String getNullField() {
        return nullField;
    }

    void setNullField(final String nullField) {
        this.nullField = nullField;
    }

}
