package webcomicreader.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import webcomicreader.webapp.model.UserComic;
import webcomicreader.webapp.storage.StorageFacade;


/**
 * Page to display a single comic.
 */
@Controller
public class ViewComicController {
    @Autowired
    private StorageFacade storage;


    @RequestMapping("/viewComic/{id}")
    public String viewComic(@PathVariable String id, Model model) {
        UserComic userComic = storage.getUserComic(id);
        model.addAttribute("comic", userComic);
        return "comicDetails";
    }

}
