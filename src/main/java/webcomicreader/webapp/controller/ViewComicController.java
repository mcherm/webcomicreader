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
import java.util.List;


/**
 * Page to display a single comic.
 */
@Controller
public class ViewComicController {
    @Autowired
    private StorageFacade storage;


    @RequestMapping("/viewComic/{userId}/{comicId}")
    public String viewComic(@PathVariable String userId, @PathVariable String comicId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("comicId", comicId);
        String userComicId = userId + "-" + comicId;

        UserComic userComic = storage.getUserComic(userComicId);
        model.addAttribute("comic", userComic);

        List<String> listsThisComicIsIn = new ArrayList<String>();
        Collection<ComicList> comicLists = storage.getComicListsForUser(userId);
        for (ComicList comicList : comicLists) {
            if (comicList.comicInList(comicId)) {
                listsThisComicIsIn.add(comicList.getTagname());
            }
        }
        model.addAttribute("listsThisComicIsIn", listsThisComicIsIn);

        return "comicDetails";
    }

}
