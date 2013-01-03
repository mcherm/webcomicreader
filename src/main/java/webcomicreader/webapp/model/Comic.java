package webcomicreader.webapp.model;

/**
 * A model object corresponding to a single comic.
 */
public interface Comic extends ObjectWithId {

    /**
     * Accessor.
     * @return the name of the comic.
     */
    public String getName();

    /**
     * Accessor.
     * @return the main home screen for this comic.
     */
    public String getHomepageURL();

}
