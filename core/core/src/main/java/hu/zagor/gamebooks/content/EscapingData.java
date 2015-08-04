package hu.zagor.gamebooks.content;

/**
 * Abstract class from which content classes can inherit. Its purpose is to provide a method to replace [ and ] characters in the xml file with < and >, making sure proper html
 * tags to be seen in the final text.
 * @author Tamas_Szekeres
 */
public abstract class EscapingData {

    /**
     * Method for replacing [ and ] characters to proper < and > characters for the html.
     * @param text the text to fix
     * @return the replaced text
     */
    protected String fixText(final String text) {
        return text == null ? null : text.replace("[", "<").replace("]", ">").replace("...", "…").replace(",,", "„").replace("''", "”").replace("``", "“")
            .replace("--", "&ndash;");
    }
}
