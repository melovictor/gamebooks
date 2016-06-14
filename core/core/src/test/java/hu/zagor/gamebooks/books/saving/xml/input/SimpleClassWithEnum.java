package hu.zagor.gamebooks.books.saving.xml.input;

import hu.zagor.gamebooks.books.saving.xml.DefaultXmlGameStateSaver;
import java.io.Serializable;

/**
 * Test class for testing {@link DefaultXmlGameStateSaver}.
 * @author Tamas_Szekeres
 */
public class SimpleClassWithEnum implements Serializable {

    private SimpleEnum enumField = SimpleEnum.KIWI;

    public SimpleEnum getEnumField() {
        return enumField;
    }

    void setEnumField(final SimpleEnum enumField) {
        this.enumField = enumField;
    }

}
