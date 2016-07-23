package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.ComplexFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import org.w3c.dom.Node;

/**
 * Transforms the fleeing element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightFleeingTransformer extends AbstractCommandSubTransformer<ComplexFightCommand<Enemy>> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ComplexFightCommand<Enemy> command,
        final ChoicePositionCounter positionCounter) {
        final FightFleeData fleeData = new FightFleeData();

        fleeData.setAfterRound(extractIntegerAttribute(node, "after", 0));
        fleeData.setText(extractAttribute(node, "text"));
        fleeData.setSufferDamage(extractBooleanAttribute(node, "sufferDamage", true));

        command.setFleeData(fleeData);
    }

}
