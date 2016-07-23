package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.ComplexFightCommand;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import org.w3c.dom.Node;

/**
 * FF-specific fight lose transformer.
 * @author Tamas_Szekeres
 */
public class FfFightLoseTransformer extends FightLoseTransformer<FfEnemy> {
    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ComplexFightCommand<FfEnemy> command,
        final ChoicePositionCounter positionCounter) {
        super.doTransform(parent, node, command, positionCounter);

        final int autoLoseRound = extractIntegerAttribute(node, "autoTriggerRound", 0);
        final int autoLoseStamina = extractIntegerAttribute(node, "autoTriggerStamina", 0);
        final FfFightCommand ffCommand = (FfFightCommand) command;
        ffCommand.setAutoLoseRound(autoLoseRound);
        ffCommand.setAutoLoseStamina(autoLoseStamina);
    }
}
