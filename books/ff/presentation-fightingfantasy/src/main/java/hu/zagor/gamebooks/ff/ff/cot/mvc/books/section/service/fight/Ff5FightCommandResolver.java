package hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.CommandResolver;
import hu.zagor.gamebooks.content.command.fight.FfFightCommandResolver;
import hu.zagor.gamebooks.content.command.fight.subresolver.FightCommandSubResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Custom fight command resolver for City of Thieves.
 * @author Tamas_Szekeres
 */
public class Ff5FightCommandResolver implements CommandResolver {

    @Autowired @Qualifier("ffFightCommandResolver") private FfFightCommandResolver superResolver;

    @Override
    public CommandResolveResult resolve(final Command commandObject, final ResolvationData resolvationData) {
        final CommandResolveResult resolve = superResolver.resolve(commandObject, resolvationData);

        if (resolve.isFinished()) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            if (hasSilverScorpionBrooch(character, resolvationData)) {
                character.changeStamina(1);
            }
        }

        return resolve;
    }

    private boolean hasSilverScorpionBrooch(final FfCharacter character, final ResolvationData resolvationData) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
        return itemHandler.hasEquippedItem(character, "3006") && characterHandler.getAttributeHandler().isAlive(character);
    }

    /**
     * Sets the subresolver map.
     * @param subResolvers the subresolvers
     */
    public void setSubResolvers(final Map<String, FightCommandSubResolver> subResolvers) {
        superResolver.setSubResolvers(subResolvers);
    }
}
