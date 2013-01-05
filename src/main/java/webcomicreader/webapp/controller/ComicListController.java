package webcomicreader.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import webcomicreader.webapp.model.UserComic;
import webcomicreader.webapp.storage.StorageFacade;

import java.util.List;


/**
 * Just show a raw list of all the comics.
 */
@Controller
public class ComicListController {

    @Autowired
    private StorageFacade storage;


    @RequestMapping("/comicList")
    public String comicList(Model model) {
        List<UserComic> comics = storage.getComics();
        model.addAttribute("comics", comics);
        return "comicList";
    }

}
