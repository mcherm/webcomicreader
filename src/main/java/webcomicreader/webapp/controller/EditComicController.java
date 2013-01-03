package webcomicreader.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import webcomicreader.webapp.model.Comic;
import webcomicreader.webapp.storage.UserComicImpl;
import webcomicreader.webapp.storage.SimpleDBStorage;
import webcomicreader.webapp.storage.UserComicSetter;


/**
 * This is a raw controller used for manually viewing and editing the data in the database.
 */
@Controller
@RequestMapping("/editComic")
public class EditComicController {
    @Autowired
    private SimpleDBStorage storage;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewComic(@PathVariable String id, Model model) {
        Comic comic = storage.getComic(id);

        model.addAttribute("id", id);
        model.addAttribute("fields", ((UserComicImpl) comic).getAllFields());
        return "editEntity";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String updateComic(@PathVariable String id, @RequestBody MultiValueMap<String,String> fields) {
        System.out.println("DOING POST");
        storage.updateUserComic(new UserComicSetter(id, fields));
        return "redirect:/viewComic/{id}";
    }
}
