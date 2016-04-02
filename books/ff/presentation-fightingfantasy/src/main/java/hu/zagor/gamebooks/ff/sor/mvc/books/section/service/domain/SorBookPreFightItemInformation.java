package hu.zagor.gamebooks.ff.sor.mvc.books.section.service.domain;

/**
 * Bean for storing usage information about a pre-fight item.
 * @author Tamas_Szekeres
 */
public class SorBookPreFightItemInformation {
    private final String itemId;
    private final String textResource;
    private final boolean reusable;
    private final boolean breakable;

    /**
     * Basic constructor.
     * @param itemId the ID of the item to use
     * @param textResource the variable part of the item's text resource key
     * @param reusable true, if the item can be used multiple times, false if it is destroyed at the first usage time
     * @param breakable true, if the item has a chance to break, false otherwise
     */
    public SorBookPreFightItemInformation(final String itemId, final String textResource, final boolean reusable, final boolean breakable) {
        this.itemId = itemId;
        this.textResource = textResource;
        this.reusable = reusable;
        this.breakable = breakable;
    }

    public String getItemId() {
        return itemId;
    }

    public String getTextResource() {
        return textResource;
    }

    public boolean isReusable() {
        return reusable;
    }

    public boolean isBreakable() {
        return breakable;
    }

}
