package webcomicreader.webapp.storage.tempmemory;

import webcomicreader.webapp.model.UserComic;

/**
 * An implementation of UserComic for in-memory storage.
 */
public class UserComicImpl implements UserComic {
    private final String id;
    private final String currentPosition;
    private final ComicImpl comic;

    public UserComicImpl(String id, ComicImpl comic, String currentPosition) {
        this.id = id;
        this.comic = comic;
        this.currentPosition = currentPosition;
    }

    @Override
    public String getCurrentPositionURL() {
        return currentPosition;
    }

    @Override
    public String getName() {
        return comic.getName();
    }

    @Override
    public String getHomepageURL() {
        return comic.getHomepageURL();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getComicId() {
        return comic.getId();
    }

    @Override
    public String getObjectType() {
        throw new RuntimeException("Not implemented");
    }
}
