package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Interface for stub commands for {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public interface ItemCheckStubCommandResolver {

    /**
     * Resolves a {@link ParagraphData} for a certain check-type.
     * @param parent the parent {@link ItemCheckCommand} object
     * @param resolvationData the {@link ResolvationData} item
     * @return the resolved {@link ParagraphData}
     */
    ParagraphData resolve(final ItemCheckCommand parent, ResolvationData resolvationData);
}
