package hu.zagor.gamebooks.domain;

/**
 * Information about extra resources required to be included on the pages.
 * @author Tamas_Szekeres
 */
public class ResourceInformation {

    private String cssResources;
    private String jsResources;

    public String getCssResources() {
        return cssResources;
    }

    public void setCssResources(final String cssResources) {
        this.cssResources = cssResources;
    }

    public String getJsResources() {
        return jsResources;
    }

    public void setJsResources(final String jsResources) {
        this.jsResources = jsResources;
    }
}
