package webcomicreader.webapp.storage;

import webcomicreader.webapp.model.ComicList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Stores the data for updating or creating a ComicList.
 */
public class ComicListSetter implements ComicList {
    private final String id;
    private final String tagname;
    private final String itemsInOrder;

    /**
     * Constructor.
     *
     * @param id the ID of the ComicList to be updated.
     * @param tagname the tagName of the ComicList
     * @param itemsInOrder a space-separated list of Comic IDs. May also have leading or trailing space.
     */
    public ComicListSetter(String id, String tagname, String itemsInOrder) {
        this.id = id;
        this.tagname = tagname;
        this.itemsInOrder = itemsInOrder.trim();
    }


    public String getId() {
        return id;
    }

    @Override
    public String getTagname() {
        return tagname;
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.asList(itemsInOrder.split(" ")).iterator();
    }

    /**
     * Method used for writing this to the DB.
     */
    public Map<String,String> getComicListFields() {
        return new TreeMap<String,String>() {{
            put("tagname", tagname);
            put("ordering", "L:" + itemsInOrder);
        }};
    }

    @Override
    public boolean comicInList(String comicId) {
        for (String x : this) {
            if (comicId.equals(x)) {
                return true;
            }
        }
        return false;
    }
}
