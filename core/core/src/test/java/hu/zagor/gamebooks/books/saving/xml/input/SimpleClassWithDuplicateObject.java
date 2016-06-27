package hu.zagor.gamebooks.books.saving.xml.input;

import java.io.Serializable;

/**
 * Test class.
 * @author Tamas_Szekeres
 */
public class SimpleClassWithDuplicateObject implements Serializable {
    private ComplexObject objectA;
    private ComplexObject objectB;

    /**
     * Default constructor for test class.
     */
    public SimpleClassWithDuplicateObject() {
        objectA = new ComplexObject();
        objectB = objectA;
    }

    public ComplexObject getObjectA() {
        return objectA;
    }

    public void setObjectA(final ComplexObject objectA) {
        this.objectA = objectA;
    }

    public ComplexObject getObjectB() {
        return objectB;
    }

    public void setObjectB(final ComplexObject objectB) {
        this.objectB = objectB;
    }

}
