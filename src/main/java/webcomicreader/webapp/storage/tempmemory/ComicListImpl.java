package webcomicreader.webapp.storage.tempmemory;

import webcomicreader.webapp.model.ComicList;

import java.util.Iterator;
import java.util.List;

/**
 * Implementation of ComicList for temp memory.
 */
public class ComicListImpl implements ComicList {
    private final String tagName;
    private final List<String> comics;

    /**
     * Implementation of ComicList for tempmemory.
     */
    public ComicListImpl(String tagName, List<String> comics) {
        this.tagName = tagName;
        this.comics = comics;
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public Iterator<String> iterator() {
        return comics.iterator();
    }
}
