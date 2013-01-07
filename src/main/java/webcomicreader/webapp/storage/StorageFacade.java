package webcomicreader.webapp.storage;

import webcomicreader.webapp.model.Comic;
import webcomicreader.webapp.model.ComicList;
import webcomicreader.webapp.model.ObjectWithId;
import webcomicreader.webapp.model.UserComic;

import java.util.Collection;
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
     * # FIXME: Deprecated
     * @return the list of Comics.
     */
    public List<UserComic> getComics();

    /**
     * Returns all lists for a given user.
     *
     * @param userId the id of the user to retrieve
     * @return a collection of the ComicLists
     */
    public Collection<ComicList> getComicListsForUser(String userId);


    /**
     * Returns all UserComics for a given user.
     *
     * @param userId the id of the user to retrieve
     * @return a map from userComicId to the UserComic
     */
    public Map<String,UserComic> getUserComicsForUser(String userId);

    /**
     * Retrieves a user-comic from the datastore, given its id.
     * @param id the Id of the user-comic to retrieve
     * @return the UserComic, or null if not found.
     */
    public UserComic getUserComic(String id);

    /**
     * Updates all values in a UserComic and also in the corresponding Comic to match
     * the values passed in. Things not existing are created.
     *
     * @param userComic the values to use.
     */
    public void updateUserComic(UserComicSetter userComic);

}
