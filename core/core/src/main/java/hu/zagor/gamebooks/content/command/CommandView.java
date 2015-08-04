package hu.zagor.gamebooks.content.command;

import java.io.Serializable;
import java.util.Map;

/**
 * Bean containing all necessary data to display a view where the user can interact with the command.
 * @author Tamas_Szekeres
 */
public class CommandView implements Serializable {

    private final String viewName;
    private final Map<String, Object> model;

    /**
     * Default constructor for the deserializer.
     */
    CommandView() {
        viewName = null;
        model = null;
    }

    /**
     * Constructor with the view name and all the model data.
     * @param viewName the name of the view to show
     * @param model the custom part of the model for the view
     */
    public CommandView(final String viewName, final Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

}
