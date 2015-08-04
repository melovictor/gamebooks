package hu.zagor.gamebooks.books.contentransforming.section;

import hu.zagor.gamebooks.books.contentransforming.section.stub.StubTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.Command;

import org.w3c.dom.Node;

/**
 * Interface for a command or command stub transformer that is capable to continue transforming a {@link Node}
 * .
 * @param <C> the exact type of the command that we're creating/modifying
 * @author Tamas_Szekeres
 */
public interface CommandSubTransformer<C extends Command> {

    /**
     * Transforms the command or command stub appropriate for the given {@link StubTransformer}.
     * @param parent the parent transformer
     * @param node the node that contains the data that has to be transformed
     * @param command the command that we're creating/finishing
     * @param positionCounter the position counter to pass along the chain
     */
    void transform(BookParagraphDataTransformer parent, Node node, C command, ChoicePositionCounter positionCounter);

}
