package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import org.w3c.dom.Node;

/**
 * Abstract class to help transforming the round bordering elements inside the fight section.
 * @author Tamas_Szekeres
 */
public abstract class FightRoundResultBorderingTransformer extends AbstractCommandSubTransformer<FightCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final FightCommand command, final ChoicePositionCounter positionCounter) {

        final RoundEvent roundEvent = getBeanFactory().getBean(RoundEvent.class);
        doRoundResultSpecificTransform(roundEvent);

        int totalCount = extractIntegerAttribute(node, "totalCount", RoundEvent.NOT_SPECIFIED);
        final int subsequentCount = extractIntegerAttribute(node, "subsequentCount", RoundEvent.NOT_SPECIFIED);

        if (totalCount == RoundEvent.NOT_SPECIFIED && subsequentCount == RoundEvent.NOT_SPECIFIED) {
            totalCount = RoundEvent.ALL_ROUNDS;
        }

        final String enemyId = extractAttribute(node, "enemy");

        roundEvent.setTotalCount(totalCount);
        roundEvent.setSubsequentCount(subsequentCount);
        roundEvent.setEnemyId(enemyId);

        final FfParagraphData paragraphData = (FfParagraphData) parent.parseParagraphData(positionCounter, node);
        roundEvent.setParagraphData(paragraphData);
        command.getRoundEvents().add(roundEvent);
    }

    /**
     * Sets up any round result specific requirements for the {@link RoundEvent} object.
     * @param roundEvent the command object to set up
     */
    protected abstract void doRoundResultSpecificTransform(RoundEvent roundEvent);

}
