package webcomicreader.webapp.model;

import java.util.Iterator;

/**
 * An ordering is a set of Comics which are maintained in a particular order.
 */
public interface Ordering {

    /**
     * Retrieves the IDs of the comics in order.
     */
    public Iterator<String> iterator();
}
