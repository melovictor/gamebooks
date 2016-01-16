package hu.zagor.gamebooks.content.command.changeitem;

import hu.zagor.gamebooks.content.command.Command;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean to store a command for changing the attributes of the items.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class ChangeItemCommand extends Command {

    private String id;
    private String attribute;
    private Integer newValue;
    private String changeValue;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(final String attribute) {
        this.attribute = attribute;
    }

    public Integer getNewValue() {
        return newValue;
    }

    public void setNewValue(final Integer newValue) {
        this.newValue = newValue;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(final String changeValue) {
        this.changeValue = changeValue;
    }

    @Override
    public String getValidMove() {
        return null;
    }

    @Override
    public ChangeItemCommand clone() throws CloneNotSupportedException {
        return (ChangeItemCommand) super.clone();
    }

    @Override
    public String toString() {
        return "ChangeItemCommand: " + id + " " + attribute + " (" + newValue + " / " + changeValue + ")";
    }
}
