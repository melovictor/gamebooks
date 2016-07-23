package hu.zagor.gamebooks.lw.section;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.content.LwParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandExecuter;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.raw.section.RawRuleBookParagraphResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of the {@link BookParagraphResolver} for the Lone Wolf ruleset.
 * @author Tamas_Szekeres
 */
public class LwRuleBookParagraphResolver extends RawRuleBookParagraphResolver {
    @Autowired private CommandExecuter immediateCommandExecuter;

    @Override
    protected void resolveBasicCommands(final ResolvationData resolvationData, final ParagraphData genericSubData) {
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        final LwParagraphData subData = (LwParagraphData) genericSubData;

        handleAttributeModifiers(character, characterHandler, subData);

        immediateCommandExecuter.execute(resolvationData, subData);
        super.resolveBasicCommands(resolvationData, subData);
    }

    void handleAttributeModifiers(final LwCharacter character, final LwCharacterHandler characterHandler, final LwParagraphData subData) {
        final List<ModifyAttribute> modifyAttributes = subData.getModifyAttributes();
        for (final ModifyAttribute modAttr : modifyAttributes) {
            characterHandler.getAttributeHandler().handleModification(character, modAttr);
        }
        modifyAttributes.clear();
    }

    @Override
    protected String getRulesetPrefix() {
        return "lw";
    }
}
