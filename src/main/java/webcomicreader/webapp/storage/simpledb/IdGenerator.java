package webcomicreader.webapp.storage.simpledb;

import java.util.Map;

/**
 * This class is responsible for generating unique IDs, using
 * a SimpleDBStorage for its source.
 */
public class IdGenerator {

    private final SimpleDBStorage storage;
    private final Map<String,Integer> currentMaxValues;

    /**
     * Constructor.
     *
     * @param storage the SimpleDBStorage that will be used to store the
     *   current max used values.
     */
    public IdGenerator(SimpleDBStorage storage) {
        this.storage = storage;
        this.currentMaxValues = storage.getLargestIdsUsed();
    }

    /**
     * This returns the next string for the given domain.
     *
     * @param domain the domain for which an ID is to be generated
     * @return the new ID. It is assumed that this value will actually
     *   be used.
     */
    public synchronized String getNextId(String domain) {
        Integer currentMaxId = currentMaxValues.get(domain);
        int newId = currentMaxId + 1;
        storage.setLargestIdUsed(domain, newId);
        return Integer.toString(newId);
    }
}
