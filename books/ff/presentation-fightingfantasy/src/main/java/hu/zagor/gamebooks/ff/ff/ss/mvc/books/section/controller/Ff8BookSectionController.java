package hu.zagor.gamebooks.ff.ff.ss.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.ss.character.Ff8Character;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.SCORPION_SWAMP)
public class Ff8BookSectionController extends FfBookSectionController {
    private static final int MAP_HEIGHT = 2384;
    private static final int MAP_WIDTH = 1393;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff8BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    /**
     * Returns the current map image to the user through the response output stream.
     * @param request the {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     * @throws IOException occurs when cannot write the data out
     */
    @RequestMapping("map")
    public void getMap(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Ff8Character character = (Ff8Character) wrapper.getCharacter();
        final BufferedImage image = getMap(character);
        response.setContentType("image/png");
        ImageIO.write(image, "png", response.getOutputStream());
    }

    private BufferedImage getMap(final Ff8Character character) throws IOException {
        final BufferedImage map = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = map.getGraphics();

        if (character != null) {
            for (final String mapPiece : character.getMaps()) {
                final Resource[] resources = getApplicationContext().getResources("classpath*:ff8/mappieces/" + mapPiece + ".gif");
                final BufferedImage piece = ImageIO.read(resources[0].getInputStream());
                graphics.drawImage(piece, 0, 0, null);
            }
        }

        return map;
    }

    @Override
    protected String doHandleRandom(final Model model, final HttpServletRequest request) {
        final String result = super.doHandleRandom(model, request);
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        if ("44".equals(paragraph.getId())) {
            final ParagraphData data = paragraph.getData();
            final String origText = data.getText();
            final Matcher matcher = Pattern.compile("diced6(\\d)").matcher(origText);
            int lower = Integer.MAX_VALUE;
            while (matcher.find()) {
                lower = Math.min(lower, Integer.parseInt(matcher.group(1)));
            }
            data.setText(origText.replace("{}", String.valueOf(lower)));
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            character.changeStamina(-lower);
            model.addAttribute("data", getCharacterPageData(wrapper.getCharacter()));
        }
        return result;
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        getInfo().getCharacterHandler().getItemHandler().removeItem(wrapper.getCharacter(), "4022", 1);
    }
}
