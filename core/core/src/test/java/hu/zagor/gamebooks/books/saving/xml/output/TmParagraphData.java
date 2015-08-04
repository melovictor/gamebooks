package hu.zagor.gamebooks.books.saving.xml.output;

import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Extended paragraph data for Time Machine books.
 * @author Tamas_Szekeres
 */
public class TmParagraphData extends ParagraphData {

    private String hint;

    public String getHint() {
        return hint;
    }

    public void setHint(final String hint) {
        this.hint = hint;
    }

}
