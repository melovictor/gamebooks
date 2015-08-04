package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;

import org.w3c.dom.Node;

/**
 * Transforms the win element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightWinTransformer extends AbstractCommandSubTransformer<FightCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final FightCommand command, final ChoicePositionCounter positionCounter) {

        final FightOutcome outcome = new FightOutcome();
        command.getWin().add(outcome);
        outcome.setMin(this.extractIntegerAttribute(node, "min", 0));
        outcome.setMax(this.extractIntegerAttribute(node, "max", Integer.MAX_VALUE));

        final FfParagraphData paragraphData = (FfParagraphData) parent.parseParagraphData(positionCounter, node);
        outcome.setParagraphData(paragraphData);
    }

}
