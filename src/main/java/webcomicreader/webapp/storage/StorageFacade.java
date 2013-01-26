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
     * Updates all values in a ComicList to match the values passed in.
     * Things not existing are created.
     *
     * @param comicList the data for the new or modified ComicList.
     */
    public void updateComicList(ComicListSetter comicList);


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
     * <p>
     * FIXME: May need to change the name; this updates UserComic *AND* the Comic also.
     *
     * @param userComic the values to use.
     */
    public void updateUserComic(UserComicSetter userComic);

    /**
     * This is passed the main fields of a Comic, and it either finds the
     * ID of the corresponding Comic or else it creates a new comic and
     * returns that ID.
     *
     * @param name the name of the Comic
     * @param homepage the URL of the homepage of the comic
     * @return the comicId of the found or created Comic
     */
    public String createOrFindComic(String name, String homepage);

    /**
     * This either creates or modifies a UserComic.
     *
     * @param userId the id of the user
     * @param comicId the id of the comic
     * @param currentPosition the current position (url)
     */
    public void createOrUpdateUserComic(String userId, String comicId, String currentPosition);

    /**
     * This updates just one field (the currentPosition field) in a UserComic.
     */
    public void updateUserComicCurrentPosition(String userComicId, String currentPosition);

    /**
     * This dumps the contents of the database to a String (in JSON format).
     */
    public String dumpdb();
}
