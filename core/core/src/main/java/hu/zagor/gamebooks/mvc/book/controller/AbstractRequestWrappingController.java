package hu.zagor.gamebooks.mvc.book.controller;

import hu.zagor.gamebooks.books.bookinfo.BookInformationFetcher;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.ResourceInformation;
import hu.zagor.gamebooks.mvc.book.controller.domain.StaticResourceDescriptor;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Abstract controller class that is capable of wrapping a {@link HttpServletRequest} or {@link HttpSession} bean into a {@link HttpSessionWrapper} bean.
 * @author Tamas_Szekeres
 */
public abstract class AbstractRequestWrappingController implements BeanFactoryAware, ApplicationContextAware {

    private static final Pattern BOOK_ID_GRABBER = Pattern.compile("^([a-z][a-zA-Z0-9]*[0-9]+)");
    private BeanFactory beanFactory;
    private BookInformations info;
    private ApplicationContext applicationContext;
    @Autowired private BookInformationFetcher bookInformationFetcher;

    /**
     * Initialization method that automatically grabs the info bean from the spring container instead of having to push it down every time.
     */
    @PostConstruct
    public void init() {
        final String bookId = fetchBookIdByReflection();
        if (bookId != null) {
            final String infoName = bookId + "Info";
            if (beanFactory.containsBean(infoName)) {
                info = beanFactory.getBean(infoName, BookInformations.class);
                info.setHelpBeanId(bookId + "Help");
            }
        }
    }

    /**
     * Gets the {@link BookInformations} bean.
     * @param bookId the ID of the {@link BookInformations} bean we're looking for
     * @return the info bean
     * @throws IllegalStateException when the bean cannot be derived from the class name
     */
    public BookInformations getInfo(final Long bookId) {
        final BookInformations info = bookInformationFetcher.getInfoById(bookId);
        if (info == null) {
            throw new IllegalStateException("The current Spring context doesn't contain a BookInformation bean for the requested ID (" + bookId + ").");
        }
        return info;
    }

    /**
     * Fetches the id of the current book using reflection.
     * @return the id of the current book, or null if the id is not identifiable.
     */
    protected String fetchBookIdByReflection() {
        final String name = this.getClass().getSimpleName();
        final String startingName = name.substring(0, 1).toLowerCase() + name.substring(1);
        final Matcher matcher = BOOK_ID_GRABBER.matcher(startingName);
        String bookId = null;
        if (matcher.find()) {
            bookId = matcher.group(1);
        }
        return bookId;
    }

    /**
     * Creates a new {@link HttpSessionWrapper} around the {@link HttpServletRequest} extracted from the given {@link HttpServletRequest} bean.
     * @param request the {@link HttpServletRequest}, cannot be null
     * @return the newly created {@link HttpSessionWrapper} object
     */
    protected final HttpSessionWrapper getWrapper(final HttpServletRequest request) {
        Assert.notNull(request, "The parameter 'request' cannot be null!");
        return (HttpSessionWrapper) beanFactory.getBean("httpSessionWrapper", request);
    }

    /**
     * Gets the {@link BookInformations} bean.
     * @return the info bean
     * @throws IllegalStateException when the bean cannot be derived from the class name
     */
    public BookInformations getInfo() {
        if (info == null) {
            throw new IllegalStateException(
                "The current Spring context doesn't contain a BookInformation bean for the current controller (" + this.getClass().getSimpleName() + ").");
        }
        return info;
    }

    /**
     * Adds a JS file to be loaded for the specific book.
     * @param model the {@link Model} object
     * @param jsName the name of the JS file
     */
    protected final void addJsResource(final Model model, final String jsName) {
        final Set<String> js = getDescriptor(model).getJs();
        final ResourceInformation resources = getInfo().getResources();
        if (resources != null) {
            addCustomResources(js, resources.getJsResources(), ".js");
        }
        js.add(jsName + ".js");
    }

    /**
     * Adds a CSS file to be loaded for the specific book.
     * @param model the {@link Model} object
     * @param cssName the name of the CSS file
     */
    protected final void addCssResource(final Model model, final String cssName) {
        final Set<String> css = getDescriptor(model).getCss();
        final ResourceInformation resources = getInfo().getResources();
        if (resources != null) {
            addCustomResources(css, resources.getCssResources(), ".css");
        }
        css.add(cssName + ".css");
    }

    private void addCustomResources(final Set<String> resourceStore, final String resources, final String extension) {
        if (!StringUtils.isEmpty(resources)) {
            for (final String resource : resources.split(",")) {
                resourceStore.add(resource + extension);
            }
        }
    }

    private StaticResourceDescriptor getDescriptor(final Model model) {
        final Map<String, Object> modelAsMap = model.asMap();
        StaticResourceDescriptor staticResourceDescriptor = (StaticResourceDescriptor) modelAsMap.get("resources");
        if (staticResourceDescriptor == null) {
            staticResourceDescriptor = beanFactory.getBean(StaticResourceDescriptor.class);
            modelAsMap.put("resources", staticResourceDescriptor);
        }
        return staticResourceDescriptor;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
