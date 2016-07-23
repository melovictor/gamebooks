package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.fight.ComplexFightCommand;
import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for transforming the "fight" elements of a paragraph.
 * @author Tamas_Szekeres
 * @param <E> the actual {@link Enemy} type used
 * @param <C> the actual {@link ComplexFightCommand} type that's going to be created
 */
public abstract class ComplexFightTransformer<E extends Enemy, C extends ComplexFightCommand<E>> extends AbstractStubTransformer {

    private Map<String, CommandSubTransformer<C>> fightTransformers;

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        data.addCommand(parseFight(parent, node, data));
    }

    private Command parseFight(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final C fightCommand = getFightCommandBean();

        parseRulesetSpecificAttributes(fightCommand, node);

        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        final ChoicePositionCounter positionCounter = data.getPositionCounter();
        for (int i = 0; i < count; i++) {
            final Node childNode = childNodes.item(i);
            if (!irrelevantNode(childNode)) {
                final String childNodeName = childNode.getNodeName();
                final CommandSubTransformer<C> responseTransformer = fightTransformers.get(childNodeName);
                if (responseTransformer == null) {
                    throw new UnsupportedOperationException(childNodeName);
                }
                responseTransformer.transform(parent, childNode, fightCommand, positionCounter);
            }
        }

        return fightCommand;
    }

    abstract void parseRulesetSpecificAttributes(C fightCommand, Node node);

    abstract C getFightCommandBean();

    public void setFightTransformers(final Map<String, CommandSubTransformer<C>> fightTransformers) {
        this.fightTransformers = fightTransformers;
    }

}
