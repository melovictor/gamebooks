package hu.zagor.gamebooks.books.contentstorage.domain;

/**
 * Enum for storing constant values of special paragraph keys that has to be used in the xml descriptors as
 * well.
 * @author Tamas_Szekeres
 */
public enum BookParagraphConstants {
    BACKGROUND("background"),
    BACK_COVER("back_cover"),
    GENERATE("generate");

    private final String value;

    private BookParagraphConstants(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
