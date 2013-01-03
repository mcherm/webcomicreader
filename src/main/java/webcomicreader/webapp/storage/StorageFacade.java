package webcomicreader.webapp.storage;

import webcomicreader.webapp.model.Comic;
import webcomicreader.webapp.model.ObjectWithId;

import java.util.List;
import java.util.Map;

/**
 * A collection of static methods that abstract storing and retrieving data
 * to and from the datastore. The current version is quite primitive (eg: no
 * users) and the API is expected to evolve over time.
 */
public interface StorageFacade {

    /**
     * Retrieves all the comics in the datastore.
     * @return the list of Comics.
     */
    public List<Comic> getComics();

    /**
     * Retrieves a comic from the datastore, given its id.
     * @param id the Id of the comic to retrieve
     * @return the Comic, or null if not found.
     */
    public Comic getComic(String id);

    public void createComic(String name);

    public void updateOneField(ObjectWithId obj, String fieldName, String fieldValue);

    public void updateSeveralFields(ObjectWithId obj, Map<String,String> fieldsToUpdate);

    public void updateSeveralFields(String domain, String id, Map<String,String> fieldsToUpdate);
}
