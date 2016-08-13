package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.CloneFailedException;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.SilentCapableResolver;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Handler for Ogre in FF60.
 * @author Tamas_Szekeres
 */
public class OgreHandler extends Ff60BeforeAfterRoundEnemyHandler {
    @Autowired @Qualifier("randomCommandResolver") private SilentCapableResolver<RandomCommand> randomCommandResolver;
    private RandomCommand ogreRandomCommand;

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        itemHandler.removeItem(character, "4001", 2);
        if (results[0] == FightRoundResult.LOSE) {
            final FightCommandMessageList messages = command.getMessages();
            try {
                messages.switchToPostRoundMessages();
                final List<ParagraphData> randomResult = randomCommandResolver.resolveSilently(ogreRandomCommand.clone(), resolvationData, messages,
                    messages.getLocale());
                if (!randomResult.isEmpty()) {
                    itemHandler.addItem(character, "4001", 2);
                    messages.addKey("page.ff60.fight.ogre.push");
                }
            } catch (final CloneNotSupportedException ex) {
                throw new CloneFailedException(ex);
            }
        }
    }

    @Override
    public String getEnemyId() {
        return "4";
    }

    public void setOgreRandomCommand(final RandomCommand ogreRandomCommand) {
        this.ogreRandomCommand = ogreRandomCommand;
    }

}
