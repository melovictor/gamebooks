package hu.zagor.gamebooks.content;

import hu.zagor.gamebooks.content.command.Command;
import java.io.Serializable;
import org.springframework.util.Assert;

/**
 * A holder that contains either a {@link Command} or a {@link ParagraphData} object for further processing.
 * @author Tamas_Szekeres
 */
public class ProcessableItemHolder implements Serializable {
    private Command command;
    private ParagraphData paragraphData;

    ProcessableItemHolder() {
    }

    /**
     * Creates a holder containing a {@link ParagraphData} object.
     * @param paragraphData the {@link ParagraphData} object, cannot be null
     */
    public ProcessableItemHolder(final ParagraphData paragraphData) {
        Assert.notNull(paragraphData, "The parameter 'paragraphData' cannot be null!");
        this.paragraphData = paragraphData;
    }

    /**
     * Creates a holder containing a {@link Command} object.
     * @param command the {@link Command} object, cannot be null
     */
    public ProcessableItemHolder(final Command command) {
        Assert.notNull(command, "The parameter 'command' cannot be null!");
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public ParagraphData getParagraphData() {
        return paragraphData;
    }

    public boolean isParagraphDataHolder() {
        return paragraphData != null;
    }

}
