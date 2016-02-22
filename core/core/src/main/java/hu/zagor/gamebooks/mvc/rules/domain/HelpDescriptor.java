package hu.zagor.gamebooks.mvc.rules.domain;

import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.ResourceInformation;
import java.util.List;
import java.util.Map;

/**
 * Bean for describing the structure of the help for a specific book.
 * @author Tamas_Szekeres
 */
public class HelpDescriptor {

    private BookInformations info;
    private List<HelpSection> sections;
    private Map<String, String> params;
    private ResourceInformation resourceInformation;

    public List<HelpSection> getSections() {
        return sections;
    }

    public void setSections(final List<HelpSection> sections) {
        this.sections = sections;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(final Map<String, String> params) {
        this.params = params;
    }

    public BookInformations getInfo() {
        return info;
    }

    public void setInfo(final BookInformations info) {
        this.info = info;
    }

    public ResourceInformation getResourceInformation() {
        return resourceInformation;
    }

    public void setResourceInformation(final ResourceInformation resourceInformation) {
        this.resourceInformation = resourceInformation;
    }

}
