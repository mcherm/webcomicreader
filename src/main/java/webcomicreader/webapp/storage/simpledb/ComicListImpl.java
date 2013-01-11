package webcomicreader.webapp.storage.simpledb;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import webcomicreader.webapp.model.ComicList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A ComicList retrieved from storage.
 */
public class ComicListImpl implements ComicList {
    private final String id;
    private final String tagname;
    private final List<String> comicIds;

    /**
     * Constructor from a comicListItem.
     * @param comicListItem the comic list item
     */
    ComicListImpl(Item comicListItem) {
        id = comicListItem.getName();
        tagname = getAttribute(comicListItem, "tagname");
        String orderingStr = getAttribute(comicListItem, "ordering");
        if (!orderingStr.startsWith("L:")) {
            throw new RuntimeException("Only explicit list orderings are supported now (item '" +
                    comicListItem.getName() + "').");
        }
        comicIds = Arrays.asList(orderingStr.substring(2).split(" "));
    }

    /**
     * Returns the specified attribute or raises an exception if there isn't one.
     * @param comicListItem the comicListItem
     * @param attributeName the name of the attribute to retrieve
     * @return the ordering attribute
     */
    private String getAttribute(Item comicListItem, String attributeName) {
        for (Attribute attribute : comicListItem.getAttributes()) {
            if (attributeName.equals(attribute.getName())) {
                return attribute.getValue();
            }
        }
        throw new RuntimeException("Comic List '" + comicListItem.getName() +
                "' does not have the attribute '" + attributeName + "'.");
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
        return comicIds.iterator();
    }

    @Override
    public boolean comicInList(String comicId) {
        return comicIds.contains(comicId);
    }
}
