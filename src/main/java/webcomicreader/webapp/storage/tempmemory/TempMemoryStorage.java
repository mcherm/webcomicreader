package webcomicreader.webapp.storage.tempmemory;

import webcomicreader.webapp.model.ComicList;
import webcomicreader.webapp.model.UserComic;
import webcomicreader.webapp.storage.ComicListSetter;
import webcomicreader.webapp.storage.StorageFacade;
import webcomicreader.webapp.storage.UserComicSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An implementation of StorageFacade that is used only for testing. It
 * simply stores data in memory and returns it as needed.
 */
public class TempMemoryStorage implements StorageFacade {

    private List<UserImpl> users;
    private List<ComicImpl> comics;
    private List<UserComicImpl> userComics;
    private List<ComicList> comicLists;


    /**
     * Constructor.
     */
    public TempMemoryStorage() {
        // --- Set up initial data ---
        users = new ArrayList<UserImpl>();
        users.add(new UserImpl("1", "mcherm"));

        comics = new ArrayList<ComicImpl>();
        comics.add(new ComicImpl("1", "XKCD", "https://www.xkcd.com/"));
        comics.add(new ComicImpl("2", "Schlock Mercenary", "http://www.schlockmercenary.com/"));
        comics.add(new ComicImpl("3", "Girl Genius", "http://www.girlgeniusonline.com/comic.php"));

        userComics = new ArrayList<UserComicImpl>();
        userComics.add(new UserComicImpl("1-1", comics.get(0), "https://www.xkcd.com/1153/"));
        userComics.add(new UserComicImpl("1-2", comics.get(1), "http://www.schlockmercenary.com/2012-12-29"));
        userComics.add(new UserComicImpl("1-3", comics.get(2), "http://www.girlgeniusonline.com/comic.php?date=20090911"));

        comicLists = new ArrayList<ComicList>();
        comicLists.add(new ComicListImpl("1-all", "all", Arrays.asList("1","3","2")));
        comicLists.add(new ComicListImpl("1-reading", "reading", Arrays.asList("2","3")));
    }


    @Override
    public List<UserComic> getComics() {
        List<UserComic> result = new ArrayList<UserComic>(userComics.size());
        result.addAll(userComics);
        return result;
    }

    @Override
    public Collection<ComicList> getComicListsForUser(String userId) {
        // FIXME: Assumes only one user in the system.
        return comicLists;
    }

    @Override
    public Map<String, UserComic> getUserComicsForUser(String userId) {
        Map<String,UserComic> result = new TreeMap<String,UserComic>();
        for (UserComicImpl userComic : userComics) {
            if (userId.equals(userComic.getId().substring(0, userComic.getId().indexOf('-')))) {
                result.put(userComic.getId(), userComic);
            }
        }
        return result;
    }

    @Override
    public UserComic getUserComic(String id) {
        for (UserComicImpl userComic : userComics) {
            if (id.equals(userComic.getId())) {
                return userComic;
            }
        }
        return null;
    }

    @Override
    public void updateUserComic(UserComicSetter data) {
        ComicImpl newComic = new ComicImpl(data.getComicId(), data.getName(), data.getHomepageURL());
        UserComicImpl newUserComic = new UserComicImpl(data.getId(), newComic, data.getCurrentPositionURL());

        boolean storedComic = false;
        for (int i=0; i<comics.size(); i++) {
            if (data.getComicId().equals(comics.get(i).getId())) {
                comics.set(i, newComic);
                storedComic = true;
                break;
            }
        }
        if (!storedComic) {
            comics.add(newComic);
        }

        boolean storedUserComic = false;
        for (int i=0; i<userComics.size(); i++) {
            if (data.getId().equals(userComics.get(i).getId())) {
                userComics.set(i, newUserComic);
                storedUserComic = true;
                break;
            }
        }
        if (!storedUserComic) {
            userComics.add(newUserComic);
        }
    }

    @Override
    public void updateComicList(ComicListSetter data) {
        List<String> comics = new ArrayList<String>();
        for (String comicId : data) {
            comics.add(comicId);
        }
        ComicListImpl newComicList = new ComicListImpl(data.getId(), data.getTagname(), comics);

        boolean storedComicList = false;
        for (int i=0; i<comicLists.size(); i++) {
            if (newComicList.getId().equals(comicLists.get(i).getId())) {
                comicLists.set(i, newComicList);
                storedComicList = true;
                break;
            }
        }
        if (!storedComicList) {
            comicLists.add(newComicList);
        }
    }
}
