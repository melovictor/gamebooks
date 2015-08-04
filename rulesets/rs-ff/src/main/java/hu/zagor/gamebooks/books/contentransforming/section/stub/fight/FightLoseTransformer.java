package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FightCommand;

import org.w3c.dom.Node;

/**
 * Transforms the lose element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightLoseTransformer extends AbstractCommandSubTransformer<FightCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final FightCommand command, final ChoicePositionCounter positionCounter) {
        final FfParagraphData paragraphData = (FfParagraphData) parent.parseParagraphData(positionCounter, node);
        final int autoLoseRound = extractIntegerAttribute(node, "autoTriggerRound", 0);
        final int autoLoseStamina = extractIntegerAttribute(node, "autoTriggerStamina", 0);
        command.setLose(paragraphData);
        command.setAutoLoseRound(autoLoseRound);
        command.setAutoLoseStamina(autoLoseStamina);
    }
}
