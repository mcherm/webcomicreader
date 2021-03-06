package webcomicreader.webapp.model;

import org.hibernate.loader.collection.CollectionInitializer;

/**
 * Parent interface for any object that has an opaque ID.
 */
public interface ObjectWithId {

    /**
     * Retrieves the ID of the object.
     * @return the ID of the object.
     */
    String getId();
}
