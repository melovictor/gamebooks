package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link SectionTextUpdater} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultSectionTextUpdater implements SectionTextUpdater {

    @Autowired
    private PositionManipulator positionManipulator;

    @Override
    public void updateParagraphContent(final Paragraph paragraph, final HuntRoundResult result) {
        final ParagraphData data = paragraph.getData();
        String text = data.getText();
        text = text.replaceAll("\\?colFirst", "").replaceAll("\\?bwFirst", "");
        text = text.replace("</div><button id=\"ff23HuntTrigger\">", result.getRoundMessage() + "</div><button id=\"ff23HuntTrigger\">");
        text = text.replaceAll("tiger.png\" class=\"g[A-H] g[0-9]{1,2}\"", "tiger.png\" class=\"" + positionManipulator.cssClassFromPosition(result.getTigerPosition())
            + "\"");
        text = text.replaceAll("dog.png\" class=\"g[A-H] g[0-9]{1,2}\" \\/>", "dog.png\" class=\"" + positionManipulator.cssClassFromPosition(result.getDogPosition())
            + "\" />");
        if (result.isHuntFinished()) {
            text += "<input type='hidden' id='ff23HuntFinished' value='" + result.getNextSectionId() + "' />";
        }
        data.setText(text);
    }

}
