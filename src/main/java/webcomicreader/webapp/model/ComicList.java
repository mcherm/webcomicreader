package webcomicreader.webapp.model;

import java.util.Iterator;
import java.util.List;

/**
 * This is a model object for an ordered set of comics.
 * Iterating it gives a list of the IDs of the comics.
 */
public interface ComicList extends Iterable<String> {

    /**
     * Returns an ID for the ComicList.
     *
     * @return the Id for the ComicList
     */
    public String getId();

    /**
     * The name of the comic list.
     *
     * @return the tag name of the comic list
     */
    public String getTagname();


    /**
     * Returns true if the given comic is in this list; false if it is not.
     *
     * @param comicId A comic id (not user-comic id).
     * @return true or false;
     */
    public boolean comicInList(String comicId);

}
