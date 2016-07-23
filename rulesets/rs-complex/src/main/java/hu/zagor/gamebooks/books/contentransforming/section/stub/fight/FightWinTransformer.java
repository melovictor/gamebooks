package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ComplexParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.ComplexFightCommand;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import org.w3c.dom.Node;

/**
 * Transforms the win element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightWinTransformer extends AbstractCommandSubTransformer<ComplexFightCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ComplexFightCommand command,
        final ChoicePositionCounter positionCounter) {

        final FightOutcome outcome = new FightOutcome();
        command.getWin().add(outcome);
        outcome.setMin(this.extractIntegerAttribute(node, "min", 0));
        outcome.setMax(this.extractIntegerAttribute(node, "max", Integer.MAX_VALUE));

        final ComplexParagraphData paragraphData = (ComplexParagraphData) parent.parseParagraphData(positionCounter, node);
        outcome.setParagraphData(paragraphData);
    }

}
