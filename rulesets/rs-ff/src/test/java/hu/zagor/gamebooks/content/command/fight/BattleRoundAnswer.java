package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import org.easymock.EasyMock;
import org.easymock.IAnswer;

/**
 * Helper class for testing the {@link FfFightCommandResolver}.
 * @author Tamas_Szekeres
 */
public class BattleRoundAnswer implements IAnswer<FightRoundResult[]> {

    private static final String BATTLE_LOG = "<p>Round #1.<!-- round 1 beforemath --><br />" + "Attack strength: 21.<br />" + "Slave's attack strength: 16.<br />"
        + "You hit slave.<!-- round 1 aftermath --></p>";

    private final FightRoundResult[] result;

    /**
     * Basic constructor with the expected response.
     * @param result the response
     */
    public BattleRoundAnswer(final FightRoundResult[] result) {
        this.result = result;
    }

    @Override
    public FightRoundResult[] answer() {
        final Object[] currentArguments = EasyMock.getCurrentArguments();
        final ResolvationData resolvationData = (ResolvationData) currentArguments[1];
        final ParagraphData rootData = resolvationData.getParagraph().getData();
        final String text = rootData.getText() + BATTLE_LOG;
        rootData.setText(text);
        return result;
    }

}
