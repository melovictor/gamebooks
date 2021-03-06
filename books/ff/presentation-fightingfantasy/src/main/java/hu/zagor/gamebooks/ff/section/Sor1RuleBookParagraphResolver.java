package hu.zagor.gamebooks.ff.section;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.List;

/**
 * Implementation of the {@link BookParagraphResolver} for the Sorcer 1 book.
 * @author Tamas_Szekeres
 */
public class Sor1RuleBookParagraphResolver extends SorRuleBookParagraphResolver {

    @Override
    void handleAttributeModifiers(final FfCharacter character, final FfCharacterHandler characterHandler, final FfParagraphData subDataObject) {
        final SorParagraphData subData = (SorParagraphData) subDataObject;
        final List<ModifyAttribute> modifyAttributes = subData.getModifyAttributes();
        for (final ModifyAttribute modAttr : modifyAttributes) {
            characterHandler.getAttributeHandler().handleModification(character, modAttr);
            if ("stamina".equals(modAttr.getAttribute()) && modAttr.getAmount() < 0 && hasManankaCurse(character, characterHandler)) {
                characterHandler.getAttributeHandler().handleModification(character, modAttr.getAttribute(), -1);
            }
        }
        modifyAttributes.clear();

        final List<ModifyAttribute> spellModifyAttributes = subData.getSpellModifyAttributes();
        for (final ModifyAttribute modAttr : spellModifyAttributes) {
            characterHandler.getAttributeHandler().handleModification(character, modAttr);
        }
        spellModifyAttributes.clear();

    }

    private boolean hasManankaCurse(final FfCharacter character, final FfCharacterHandler characterHandler) {
        return characterHandler.getItemHandler().hasItem(character, "5001");
    }

}
