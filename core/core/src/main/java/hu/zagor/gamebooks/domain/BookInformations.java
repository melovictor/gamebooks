package hu.zagor.gamebooks.domain;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolver;
import hu.zagor.gamebooks.support.bookids.english.Series;
import java.util.Locale;
import java.util.Map;

/**
 * Bean for storing book informations that can be used to display to the user.
 * @author Tamas_Szekeres
 */
public class BookInformations implements Comparable<BookInformations> {

    private final Long id;
    private String title;
    private String series;
    private Double position;
    private String coverPath;
    private Locale locale;
    private String resourceDir;
    private BookContentFiles contents;
    private BookContentTransformers contentTransformers;
    private BookParagraphResolver paragraphResolver;
    private BookContentSpecification contentSpecification;
    private Map<Class<? extends Command>, CommandResolver> commandResolvers;
    private boolean disabled;
    private boolean hidden;
    private boolean unfinished;
    private String helpBeanId;
    private ContinuationData continuationData;

    private String characterBeanId = "character";
    private String characterPageDataBeanId = "rawCharacterPageData";

    private ResourceInformation resources;

    private CharacterHandler characterHandler;

    /**
     * Basic constructor that sets the id of the book instance.
     * @param id the unique id of the book
     */
    public BookInformations(final long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(final String series) {
        this.series = series;
    }

    public Double getPosition() {
        return position;
    }

    public void setPosition(final Double order) {
        position = order;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(final String coverPath) {
        this.coverPath = coverPath;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public Long getId() {
        return id;
    }

    public BookContentFiles getContents() {
        return contents;
    }

    public void setContents(final BookContentFiles contents) {
        this.contents = contents;
    }

    public BookParagraphResolver getParagraphResolver() {
        return paragraphResolver;
    }

    public void setParagraphResolver(final BookParagraphResolver paragraphResolver) {
        this.paragraphResolver = paragraphResolver;
    }

    public BookContentTransformers getContentTransformers() {
        return contentTransformers;
    }

    public void setContentTransformers(final BookContentTransformers contentTransformers) {
        this.contentTransformers = contentTransformers;
    }

    public BookContentSpecification getContentSpecification() {
        return contentSpecification;
    }

    public void setContentSpecification(final BookContentSpecification contentSpecification) {
        this.contentSpecification = contentSpecification;
    }

    public String getResourceDir() {
        return resourceDir;
    }

    public void setResourceDir(final String resourceDir) {
        this.resourceDir = resourceDir;
    }

    public CharacterHandler getCharacterHandler() {
        return characterHandler;
    }

    public void setCharacterHandler(final CharacterHandler characterHandler) {
        this.characterHandler = characterHandler;
    }

    public Map<Class<? extends Command>, CommandResolver> getCommandResolvers() {
        return commandResolvers;
    }

    public void setCommandResolvers(final Map<Class<? extends Command>, CommandResolver> commandResolvers) {
        this.commandResolvers = commandResolvers;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(final boolean enabled) {
        this.disabled = enabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isUnfinished() {
        return unfinished;
    }

    public void setUnfinished(final boolean unfinished) {
        this.unfinished = unfinished;
    }

    @Override
    public int compareTo(final BookInformations o) {
        int compareTo = position.compareTo(o.position);
        if (compareTo == 0) {
            compareTo = title.compareTo(o.title);
        }
        return compareTo;
    }

    public String getHelpBeanId() {
        return helpBeanId;
    }

    public void setHelpBeanId(final String helpBeanId) {
        this.helpBeanId = helpBeanId;
    }

    public String getCharacterBeanId() {
        return characterBeanId;
    }

    public void setCharacterBeanId(final String characterBeanId) {
        this.characterBeanId = characterBeanId;
    }

    public String getCharacterPageDataBeanId() {
        return characterPageDataBeanId;
    }

    public void setCharacterPageDataBeanId(final String characterPageDataBeanId) {
        this.characterPageDataBeanId = characterPageDataBeanId;
    }

    public ResourceInformation getResources() {
        return resources;
    }

    public void setResources(final ResourceInformation resources) {
        this.resources = resources;
    }

    public Long getSeriesId() {
        return id / Series.SERIES_MULTIPLIER;
    }

    public ContinuationData getContinuationData() {
        return continuationData;
    }

    public void setContinuationData(final ContinuationData continuationData) {
        this.continuationData = continuationData;
    }

}
