package webcomicreader.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import webcomicreader.webapp.model.ComicList;
import webcomicreader.webapp.model.UserComic;
import webcomicreader.webapp.storage.StorageFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Displays the home page for a user.
 */
@Controller
public class UserHomepageController {
    @Autowired
    private StorageFacade storage;


    @RequestMapping("/userHomepage/{userId}")
    public String comicList(@PathVariable String userId, Model model) {
        Collection<ComicList> comicLists = storage.getComicListsForUser(userId);
        Map<String,UserComic> allUserComics = storage.getUserComicsForUser(userId);
        model.addAttribute("comicLists", comicLists);

        Map<String,List<UserComic>> comicListComics = new HashMap<String,List<UserComic>>();
        for (ComicList comicList : comicLists) {
            List<UserComic> listUserComics = new ArrayList<UserComic>();
            for (String comicId : comicList){
                String userComicId = userId + "-" + comicId;
                UserComic userComic = allUserComics.get(userComicId);
                if (userComic == null) {
                    throw new RuntimeException("List contains a comic that is no longer there.");
                }
                listUserComics.add(userComic);
            }
            comicListComics.put(comicList.getId(), listUserComics);
        }
        model.addAttribute("comicListComics", comicListComics);
        return "userHomepage";
    }
}
