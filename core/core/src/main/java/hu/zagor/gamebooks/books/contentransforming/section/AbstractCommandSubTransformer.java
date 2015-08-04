package hu.zagor.gamebooks.books.contentransforming.section;

import hu.zagor.gamebooks.books.contentransforming.AbstractTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;

import org.springframework.util.Assert;
import org.w3c.dom.Node;

/**
 * Abstract {@link CommandSubTransformer} implementation that adds input parameter validation.
 * @author Tamas_Szekeres
 * @param <C> the exact type of the command that we're creating/modifying
 */
public abstract class AbstractCommandSubTransformer<C extends Command> extends AbstractTransformer implements CommandSubTransformer<C> {

    @Override
    public final void transform(final BookParagraphDataTransformer parent, final Node node, final C command,
            final ChoicePositionCounter positionCounter) {
        Assert.notNull(parent, "The parameter 'parent' cannot be null!");
        Assert.notNull(node, "The parameter 'node' cannot be null!");
        Assert.notNull(command, "The parameter 'command' cannot be null!");
        Assert.notNull(positionCounter, "The parameter 'positionCounter' cannot be null!");

        doTransform(parent, node, command, positionCounter);
    }

    /**
     * Does the actual transformation.
     * @param parent the parent {@link BookParagraphDataTransformer}
     * @param node the {@link Node} to transform
     * @param command the {@link UserInputCommand} into which we want to transform the contents of the node
     * @param positionCounter the {@link ChoicePositionCounter}
     */
    protected abstract void doTransform(final BookParagraphDataTransformer parent, final Node node, final C command,
            final ChoicePositionCounter positionCounter);

}
