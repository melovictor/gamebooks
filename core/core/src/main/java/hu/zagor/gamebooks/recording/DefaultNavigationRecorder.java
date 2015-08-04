package hu.zagor.gamebooks.recording;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link NavigationRecorder} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultNavigationRecorder extends AbstractPlaybackEventRecorder implements NavigationRecorder {

    @Autowired
    private EnvironmentDetector environmentDetector;

    @Override
    public void recordNavigation(final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph previousParagraph, final Paragraph paragraph) {
        wrapper.getModel().addAttribute("environmentDetector", environmentDetector);
        if (environmentDetector.isRecordState()) {
            doRecordNavigation(wrapper, previousParagraph, paragraph, sectionIdentifier != null);
        }
    }

    private void doRecordNavigation(final HttpSessionWrapper wrapper, final Paragraph previousParagraph, final Paragraph paragraph, final boolean shouldRecordGoTo) {
        final StringBuilder builder = new StringBuilder();
        if (shouldRecordGoTo) {
            final Choice choice = previousParagraph.getData().getChoices().getChoiceById(paragraph.getId());
            if (choice != null) {
                builder.append("goToPosition(" + choice.getPosition() + ", " + paragraph.getDisplayId() + ");\n");
            }
        }
        if (paragraph.getData().getCommands().isEmpty()) {
            for (final Choice choice : paragraph.getData().getChoices()) {
                builder.append("verifyPosition(" + choice.getPosition() + ", " + choice.getDisplay() + ");\n");
            }
        }
        if (builder.length() > 0) {
            add(wrapper, builder);
        }
    }
}
