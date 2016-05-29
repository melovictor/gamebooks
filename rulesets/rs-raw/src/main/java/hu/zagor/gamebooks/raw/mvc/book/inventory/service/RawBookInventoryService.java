package hu.zagor.gamebooks.raw.mvc.book.inventory.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Basic service for handling the inventory display.
 * @author Tamas_Szekeres
 */
@Component
public class RawBookInventoryService implements BeanFactoryAware, BookInventoryService {

    @Autowired @Qualifier("rawSectionHandlingService") private SectionHandlingService sectionHandlingService;
    private BeanFactory beanFactory;

    @Override
    public String handleInventory(final Model model, final HttpSessionWrapper wrapper, final BookInformations info) {
        final PlayerUser player = wrapper.getPlayer();
        sectionHandlingService.initModel(model, player, info);

        final Character c = wrapper.getCharacter();
        model.addAttribute("data", getCharacterPageData(c, info));
        model.addAttribute("bookInfo", info);

        return "rawCharPage";
    }

    /**
     * Provides the transfer object bringing the character's properties to the frontend.
     * @param character the character
     * @param info the {@link BookInformations} object
     * @return the transfer object
     */
    protected RawCharacterPageData getCharacterPageData(final Character character, final BookInformations info) {
        return (RawCharacterPageData) beanFactory.getBean(info.getCharacterPageDataBeanId(), character);
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

}
