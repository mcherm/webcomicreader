package webcomicreader.webapp.model;

/**
 * The data specific to a comic INCLUDING that specific to a particular user.
 */
public interface UserComic extends Comic {
    /**
     * Accessor.
     * @return the current position (for this user) in the comic.
     */
    public String getCurrentPositionURL();
}
