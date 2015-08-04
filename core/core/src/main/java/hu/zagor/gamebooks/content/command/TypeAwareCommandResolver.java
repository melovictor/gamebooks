package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;

/**
 * An abstract class that first converts the generic {@link Command} bean into the specific bean so the child
 * {@link CommandResolver} classes wouldn't need to worry about this.
 * @param <E> the actual Command type to be resolved
 * @author Tamas_Szekeres
 */
public abstract class TypeAwareCommandResolver<E extends Command> implements CommandResolver {

    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private DiceResultRenderer diceRenderer;
    @Autowired
    private LocaleProvider localeProvider;

    @Override
    public CommandResolveResult resolve(final Command commandObject, final ResolvationData resolvationData) {
        @SuppressWarnings("unchecked")
        final E command = (E) commandObject;
        return doResolveWithResolver(command, resolvationData);
    }

    /**
     * The actual resolvation of the command.
     * @param command the specific {@link Command} to resolve
     * @param resolvationData all the data required for resolving the current command
     * @return {@link CommandResolveResult} containing a list of {@link ParagraphData} beans that also needs
     *         to be resolved afterwards by the main {@link BookParagraphResolver} (can be null or empty as
     *         well)
     */
    protected CommandResolveResult doResolveWithResolver(final E command, final ResolvationData resolvationData) {
        final List<ParagraphData> resolveList = doResolve(command, resolvationData);
        final CommandResolveResult result = new CommandResolveResult();
        result.setResolveList(resolveList);
        return result;
    }

    /**
     * The actual resolvation of the command.
     * @param command the specific {@link Command} to resolve
     * @param resolvationData all the data required for resolving the current command
     * @return a list of {@link ParagraphData} beans that also need to be resolved afterwards by the main
     *         {@link BookParagraphResolver}, an empty list if no paragraph has to be resolved but also no
     *         (more) user interaction is required for the successful resolvation of the current command, or
     *         null when user-interaction is required for resolving the {@link Command} properly
     */
    protected abstract List<ParagraphData> doResolve(final E command, final ResolvationData resolvationData);

    /**
     * Appends new text into the provided {@link ParagraphData} object.
     * @param data the {@link ParagraphData} into which the text should be appended
     * @param label the text that should be appended
     * @param prefix true if the new text must be inserted to the beginning of the data, false if it must be
     *        inserted to the end
     */
    protected void appendText(final ParagraphData data, final String label, final boolean prefix) {
        final String currentText = data.getText();
        final String newText = "[p]" + label + "[/p]";
        String combinedText;

        if (currentText == null || currentText.length() == 0) {
            combinedText = newText;
        } else {
            if (prefix) {
                combinedText = newText + currentText;
            } else {
                combinedText = currentText + newText;
            }
        }

        data.setText(combinedText);
    }

    public HierarchicalMessageSource getMessageSource() {
        return messageSource;
    }

    public DiceResultRenderer getDiceRenderer() {
        return diceRenderer;
    }

    public LocaleProvider getLocaleProvider() {
        return localeProvider;
    }
}
