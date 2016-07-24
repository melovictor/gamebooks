package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import org.w3c.dom.Node;

/**
 * LW-specific random transformer class.
 * @author Tamas_Szekeres
 */
public class LwRandomTransformer extends RandomTransformer {
    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final String diceConfig = extractAttribute(node, "diceConfig", "1d10");

        super.doTransform(parent, node, data);
        final CommandList commands = data.getCommands();
        final RandomCommand command = (RandomCommand) commands.get(commands.size() - 1);

        command.setDiceConfig("dice" + diceConfig);

    }
}
