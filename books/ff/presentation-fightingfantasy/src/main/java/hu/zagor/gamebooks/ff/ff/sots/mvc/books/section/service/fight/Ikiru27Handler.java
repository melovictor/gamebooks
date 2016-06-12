package hu.zagor.gamebooks.ff.ff.sots.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Handles Ikiru at section 27.
 * @author Tamas_Szekeres
 */
@Component
public class Ikiru27Handler extends Ff20BeforeAfterRoundEnemyHandler {

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final BasicEnemyPrePostFightDataContainer data) {
        final boolean lucky = testLuck(resolvationData, command);
        if (!lucky) {
            gainItem(resolvationData, "4010");
            killEnemy(data.getCurrentEnemy());
        }
    }

    private boolean testLuck(final ResolvationData resolvationData, final FightCommand command) {
        boolean lucky;

        final int[] randomNumber = getGenerator().getRandomNumber(2);
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final int luck = resolvationData.getCharacterHandler().getAttributeHandler().resolveValue(character, "luck");
        lucky = randomNumber[0] <= luck;
        character.changeLuck(-1);

        final FightCommandMessageList messages = command.getMessages();

        final String resultText = messages.resolveKey("page.ff.label.test." + (lucky ? "success" : "failure"));
        messages.addKey("page.ff.label.test.luck.compact", getRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber), randomNumber[0], resultText);

        return lucky;
    }

    private void gainItem(final ResolvationData resolvationData, final String itemId) {
        final Character character = resolvationData.getCharacter();
        resolvationData.getCharacterHandler().getItemHandler().addItem(character, itemId, 1);
    }

    @Override
    protected String getEnemyId() {
        return "68";
    }
}
