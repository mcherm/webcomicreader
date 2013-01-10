package webcomicreader.webapp.model;

/**
 * The data specific to a comic INCLUDING that specific to a particular user.
 */
public interface UserComic extends Comic {

    /**
     * Returns the Id of the Comic that this UserComic pertains to.
     * @return the Id of a Comic.
     */
    public String getComicId();

    /**
     * Accessor.
     * @return the current position (for this user) in the comic.
     */
    public String getCurrentPositionURL();
}
