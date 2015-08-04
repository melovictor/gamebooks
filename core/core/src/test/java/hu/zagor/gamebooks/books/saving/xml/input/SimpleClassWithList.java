package hu.zagor.gamebooks.books.saving.xml.input;

import hu.zagor.gamebooks.books.saving.xml.DefaultXmlGameStateSaver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for testing {@link DefaultXmlGameStateSaver}.
 * @author Tamas_Szekeres
 */
public class SimpleClassWithList implements Serializable {

    private List<String> elements;

    /**
     * Default constructor to add some elements to the list.
     */
    public SimpleClassWithList() {
        elements = new ArrayList<>();
        elements.add("apple");
        elements.add("pear");
    }

    public List<String> getElements() {
        return elements;
    }

    void setElements(final List<String> elements) {
        this.elements = elements;
    }

}
