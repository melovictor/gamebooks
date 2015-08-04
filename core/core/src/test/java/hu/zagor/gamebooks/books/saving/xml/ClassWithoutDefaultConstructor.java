package hu.zagor.gamebooks.books.saving.xml;

/**
 * Test class without default constructor.
 * @author Tamas_Szekeres
 */
public class ClassWithoutDefaultConstructor {

    private final int intField;

    /**
     * Basic constructor.
     * @param intField a number
     */
    public ClassWithoutDefaultConstructor(final int intField) {
        this.intField = intField;
    }

    public int getIntField() {
        return intField;
    }
}
