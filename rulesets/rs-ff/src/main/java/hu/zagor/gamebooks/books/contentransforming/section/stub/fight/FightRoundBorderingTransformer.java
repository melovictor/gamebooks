package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import org.w3c.dom.Node;

/**
 * Transforms the afterRound element inside the fight section.
 * @author Tamas_Szekeres
 */
public abstract class FightRoundBorderingTransformer extends AbstractCommandSubTransformer<FfFightCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final FfFightCommand command, final ChoicePositionCounter positionCounter) {
        final FfParagraphData paragraphData = (FfParagraphData) parent.parseParagraphData(positionCounter, node);
        final FightRoundBoundingCommand fightBounding = new FightRoundBoundingCommand(command);

        fightBounding.setLuckAllowed(extractBooleanAttribute(node, "canUseLuck", false));
        fightBounding.setNth(extractIntegerAttribute(node, "everyNth", 1));
        fightBounding.getCommands().addAll(paragraphData.getCommands());

        setBounding(command, fightBounding);
    }

    /**
     * Method to set the {@link FightRoundBoundingCommand} bean into the proper slot in the {@link FfFightCommand} descriptor.
     * @param command the {@link FfFightCommand} into which the bean has to be set
     * @param fightRandom the {@link FightRoundBoundingCommand} that has to be set
     */
    protected abstract void setBounding(FfFightCommand command, FightRoundBoundingCommand fightRandom);

}
