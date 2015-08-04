package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.itemcheck.CheckType;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;

import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for transforming item check sections of a paragraph.
 * @author Tamas_Szekeres
 */
public class ItemCheckTransformer extends AbstractStubTransformer {

    private Map<String, CommandSubTransformer<ItemCheckCommand>> stubs;

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        data.addCommand(parseItemCheck(parent, node, data.getPositionCounter()));
    }

    private ItemCheckCommand parseItemCheck(final BookParagraphDataTransformer parent, final Node node, final ChoicePositionCounter positionCounter) {
        final ItemCheckCommand command = getBeanFactory().getBean(ItemCheckCommand.class);

        command.setCheckType(CheckType.valueOf(extractAttribute(node, "type")));
        command.setId(extractAttribute(node, "id"));
        command.setAmount(extractIntegerAttribute(node, "amount", 1));
        checkHaveAttribute(parent, node, command, positionCounter);
        checkDontHaveAttribute(parent, node, command, positionCounter);

        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            final Node childNode = childNodes.item(i);
            if (!irrelevantNode(childNode)) {
                transformNode(parent, positionCounter, command, childNode);
            }
        }

        return command;
    }

    private void checkHaveAttribute(final BookParagraphDataTransformer parent, final Node node, final ItemCheckCommand command,
        final ChoicePositionCounter positionCounter) {
        final String have = extractAttribute(node, "have");
        if (have != null) {
            final ParagraphData paragraphData = createParagraphData(parent, have, positionCounter.updateAndGetPosition(null));
            command.setHave(paragraphData);
            command.setHaveEquipped(paragraphData);
        }
    }

    private void checkDontHaveAttribute(final BookParagraphDataTransformer parent, final Node node, final ItemCheckCommand command,
        final ChoicePositionCounter positionCounter) {
        final String dontHave = extractAttribute(node, "dontHave");
        if (dontHave != null) {
            final ParagraphData paragraphData = createParagraphData(parent, dontHave, positionCounter.updateAndGetPosition(null));
            command.setDontHave(paragraphData);
        }
    }

    private ParagraphData createParagraphData(final BookParagraphDataTransformer parent, final String id, final int position) {
        final ParagraphData paragraphData = parent.getParagraphData();
        paragraphData.addChoice(new Choice(id, null, position, null));
        return paragraphData;
    }

    private void transformNode(final BookParagraphDataTransformer parent, final ChoicePositionCounter positionCounter, final ItemCheckCommand command,
        final Node childNode) {
        final String nodeName = childNode.getNodeName();
        final CommandSubTransformer<ItemCheckCommand> stubTransformer = stubs.get(nodeName);
        if (stubTransformer != null) {
            stubTransformer.transform(parent, childNode, command, positionCounter);
        } else {
            throw new UnsupportedOperationException(nodeName);
        }
    }

    public void setStubs(final Map<String, CommandSubTransformer<ItemCheckCommand>> stubs) {
        this.stubs = stubs;
    }

}
