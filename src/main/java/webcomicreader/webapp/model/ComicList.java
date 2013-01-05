package webcomicreader.webapp.model;

import java.util.Iterator;
import java.util.List;

/**
 * This is a model object for an ordered set of comics.
 * Iterating it gives a list of the IDs of the comics.
 */
public interface ComicList extends Iterable<String> {

    /**
     * The name of the comic list.
     *
     * @return the tag name of the comic list
     */
    public String getTagName();

}
