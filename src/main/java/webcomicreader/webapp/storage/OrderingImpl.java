package webcomicreader.webapp.storage;

import webcomicreader.webapp.model.Ordering;

import java.util.Arrays;
import java.util.Iterator;

/**
 * An implementation for Ordering.
 */
public class OrderingImpl implements Ordering {
    private final String[] ids;

    /**
     * Constructor used when the ordering is short enough to fit in a single
     * field.
     * @param orderingAsField a space-separated list of ids.
     */
    public OrderingImpl(String orderingAsField) {
        ids = orderingAsField.split(" ");
    }


    @Override
    public Iterator<String> iterator() {
        return Arrays.asList(ids).iterator();
    }
}
