package hu.zagor.gamebooks.content.command.userinput;

import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User input command.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class UserInputCommand extends Command {

    private String label;
    private List<UserInputResponse> responses = new ArrayList<>();
    private boolean ongoing;
    private String type;

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * Adds a new possible response object to the {@link UserInputCommand}.
     * @param response the new response object
     */
    public void addResponse(final UserInputResponse response) {
        responses.add(response);
    }

    @Override
    public CommandView getCommandView(final String rulesetPrefix) {
        final Map<String, Object> model = new HashMap<>();
        model.put("userInputLabel", label);
        model.put("responseType", type);

        if (ongoing) {
            hideChoices(model);
        }

        return new CommandView(rulesetPrefix + "UserInputCommand", model);
    }

    @Override
    public UserInputCommand clone() throws CloneNotSupportedException {
        final UserInputCommand cloned = (UserInputCommand) super.clone();

        cloned.responses = new ArrayList<>();
        for (final UserInputResponse response : responses) {
            cloned.responses.add(response.clone());
        }

        return cloned;
    }

    List<UserInputResponse> getResponses() {
        return responses;
    }

    @Override
    public String toString() {
        return "UserInputCommand: " + label;
    }

    @Override
    public String getValidMove() {
        return "userInputResponse";
    }

    public void setOngoing(final boolean ongoing) {
        this.ongoing = ongoing;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
