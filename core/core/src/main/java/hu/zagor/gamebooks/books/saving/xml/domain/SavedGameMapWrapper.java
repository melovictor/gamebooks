package hu.zagor.gamebooks.books.saving.xml.domain;

/**
 * Wrapper object for the map to save it properly.
 * @author Tamas_Szekeres
 */
public class SavedGameMapWrapper {

    private Object element;

    /**
     * Default constructor.
     */
    public SavedGameMapWrapper() {
    }

    /**
     * Basic constructor that fills the object to store.
     * @param element the object to store
     */
    public SavedGameMapWrapper(final Object element) {
        this.element = element;
    }

    public Object getElement() {
        return element;
    }

}
