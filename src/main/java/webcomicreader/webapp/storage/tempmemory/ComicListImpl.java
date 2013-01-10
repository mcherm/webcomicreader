package webcomicreader.webapp.storage.tempmemory;

import webcomicreader.webapp.model.ComicList;

import java.util.Iterator;
import java.util.List;

/**
 * Implementation of ComicList for temp memory.
 */
public class ComicListImpl implements ComicList {
    private final String id;
    private final String tagname;
    private final List<String> comics;

    /**
     * Implementation of ComicList for tempmemory.
     */
    public ComicListImpl(String id, String tagname, List<String> comics) {
        this.id = id;
        this.tagname = tagname;
        this.comics = comics;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTagname() {
        return tagname;
    }

    @Override
    public Iterator<String> iterator() {
        return comics.iterator();
    }
}
