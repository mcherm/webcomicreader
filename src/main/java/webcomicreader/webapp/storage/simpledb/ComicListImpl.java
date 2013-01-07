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
    private final String tagname;
    private final List<String> comicIds;

    /**
     * Constructor from a comicListItem.
     * @param comicListItem the comic list item
     */
    ComicListImpl(Item comicListItem) {
        tagname = comicListItem.getName();
        String orderingStr = getOrderingAttribute(comicListItem);
        if (!orderingStr.startsWith("L:")) {
            throw new RuntimeException("Only explicit list orderings are supported now (item '" +
                    comicListItem.getName() + "').");
        }
        comicIds = Arrays.asList(orderingStr.substring(2).split(" "));
    }

    /**
     * Returns the ordering attribute or raises an exception if there isn't one.
     * @param comicListItem the comicListItem
     * @return the ordering attribute
     */
    private String getOrderingAttribute(Item comicListItem) {
        for (Attribute attribute : comicListItem.getAttributes()) {
            if ("ordering".equals(attribute.getName())) {
                return attribute.getValue();
            }
        }
        throw new RuntimeException("Comic List '" + comicListItem.getName() + "' has no ordering.");
    }

    @Override
    public String getTagName() {
        return tagname;
    }

    @Override
    public Iterator<String> iterator() {
        return comicIds.iterator();
    }
}
