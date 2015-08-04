package hu.zagor.gamebooks.books.saving.xml.input;

import hu.zagor.gamebooks.books.saving.xml.DefaultXmlGameStateSaver;

/**
 * Test class for testing {@link DefaultXmlGameStateSaver}.
 * @author Tamas_Szekeres
 */
public class SimpleClassWithNumbers extends SimpleClassWithInt {

    private static final double DOUBLE_INITIAL_VALUE = 1.1;
    private double doubleField = DOUBLE_INITIAL_VALUE;
    private long longField = 1L;
    private boolean booleanField = true;
    private String stringField = "bla";

    public double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(final double doubleField) {
        this.doubleField = doubleField;
    }

    public long getLongField() {
        return longField;
    }

    public void setLongField(final long longField) {
        this.longField = longField;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public void setBooleanField(final boolean booleanField) {
        this.booleanField = booleanField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(final String stringField) {
        this.stringField = stringField;
    }

}
