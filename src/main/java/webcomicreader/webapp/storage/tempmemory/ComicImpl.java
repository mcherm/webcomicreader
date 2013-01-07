package webcomicreader.webapp.storage.tempmemory;

import webcomicreader.webapp.model.Comic;

/**
 * An implementation of comic for tempmemory.
 */
public class ComicImpl implements Comic {
    private final String id;
    private final String name;
    private final String homepage;

    /**
     * Constructor.
     */
    public ComicImpl(String id, String name, String homepage) {
        this.id = id;
        this.name = name;
        this.homepage = homepage;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHomepageURL() {
        return homepage;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getObjectType() {
        throw new RuntimeException("Not implemented");
    }
}
