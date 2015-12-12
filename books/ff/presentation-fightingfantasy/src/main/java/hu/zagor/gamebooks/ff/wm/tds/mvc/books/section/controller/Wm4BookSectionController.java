package hu.zagor.gamebooks.ff.wm.tds.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommandResolver;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.ff.wm.tds.content.command.attributetest.LyingAttributeTestCommand;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Warlock;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Warlock.THE_DERVISH_STONE)
public class Wm4BookSectionController extends FfBookSectionController {

    private static final String NOMAD_ROBBERY = "69";
    @Autowired private AttributeTestCommandResolver attribTester;
    @Autowired private LocaleProvider localeProvider;
    @Autowired private HierarchicalMessageSource messageSource;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Wm4BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    @RequestMapping(value = PageAddresses.ATTRIBUTE_TEST)
    public String handleAttributeTest(final Model model, final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        if (NOMAD_ROBBERY.equals(paragraph.getId())) {
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            final Iterator<Item> itemIterator = getInfo().getCharacterHandler().getItemHandler().getItemIterator(character);
            final LyingAttributeTestCommand command = setUpCommand();
            final ResolvationData resolvationData = DefaultResolvationDataBuilder.builder().withRootData(paragraph.getData()).withBookInformations(getInfo())
                .withCharacter(character).build();
            final Locale locale = localeProvider.getLocale();
            final StringBuilder builder = new StringBuilder("[p class='wm4HideNexPar']");
            final List<String> messages = new ArrayList<>();
            while (itemIterator.hasNext()) {
                final Item item = itemIterator.next();
                if (!"defWpn".equals(item.getId())) {
                    command.reset();
                    attribTester.resolveSilently(command, resolvationData, messages, locale);
                    character.changeLuck(1);
                    if (command.getResult() >= command.getAgainstNumeric()) {
                        itemIterator.remove();
                    }
                    builder.append(getMessage(command, locale, item.getName()) + "<br />\n");
                }
            }
            final String totalMessages = builder.toString();
            if (!totalMessages.isEmpty()) {
                paragraph.getData().appendText(totalMessages + "[/p]");
            }
        }

        final String handlingResult = super.handleSection(model, request, null);

        return handlingResult;
    }

    private LyingAttributeTestCommand setUpCommand() {
        final LyingAttributeTestCommand command = new LyingAttributeTestCommand();
        command.setAgainst("luck");
        command.setConfigurationName("dice2d6");
        return command;
    }

    private String getMessage(final AttributeTestCommand command, final Locale locale, final String itemName) {
        final String itemStateResult = messageSource.getMessage("page.wm4.itemLosing." + (command.getResult() < command.getAgainstNumeric() ? "remain" : "lost"),
            new Object[]{itemName}, locale);
        return messageSource.getMessage("page.ff.label.test.luck.compact", new Object[]{command.getResultString(), command.getResult(), itemStateResult}, locale);
    }
}
