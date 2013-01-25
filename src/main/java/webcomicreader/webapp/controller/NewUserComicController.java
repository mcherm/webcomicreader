package webcomicreader.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import webcomicreader.webapp.model.ComicList;
import webcomicreader.webapp.storage.ComicListSetter;
import webcomicreader.webapp.storage.StorageFacade;
import webcomicreader.webapp.storage.UserComicSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is a controller for the creation of and editing of a UserComic.
 * For the time being it assumes that every UserComic creates a brand
 * new Comic (sharing those will be a later idea).
 */
@Controller
@RequestMapping("/newUserComic")
public class NewUserComicController {

    @Autowired
    private StorageFacade storage;


    /**
     * This just draws the screen. It prepopulates any fields that were
     * included as request parameters.
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public String showPageForMakingANewUserComic(
            @PathVariable String userId,
            @RequestParam String addToLists,
            Model model)
    {
        model.addAttribute("userId", userId);
        model.addAttribute("addToLists", addToLists);
        return "newUserComic";
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public String createNewUserComic(
            @PathVariable String userId,
            @RequestParam String name,
            @RequestParam String homepage,
            @RequestParam String addToLists)
    {
        // --- Create comic ---
        String newComicId = storage.createOrFindComic(name, homepage);

        // --- Create user-comic ---
        String currentPosition = homepage;
        storage.createOrUpdateUserComic(userId, newComicId, currentPosition);

        // --- Add to lists ---
        Set<String> listsToBeAddedTo = new TreeSet<String>(Arrays.asList(addToLists.trim().split(" ")));
        listsToBeAddedTo.add("all");
        Collection<ComicList> comicLists = storage.getComicListsForUser(userId); // FIXME: Shouldn't need ALL of them
        for (String listTag : listsToBeAddedTo) {
            List<String> comicsInList = new ArrayList<String>();
            for (ComicList comicList : comicLists) {
                if(comicList.getTagname().equals(listTag)) {
                    for (String x : comicList) {
                        comicsInList.add(x);
                    }
                    break;
                }
            }
            comicsInList.add(newComicId); // add new one at the end
            StringBuilder itemsInOrder = new StringBuilder();
            for (String x : comicsInList) {
                itemsInOrder.append(x);
                itemsInOrder.append(' ');
            }
            String comicListId = userId + '-' + listTag;
            storage.updateComicList(new ComicListSetter(comicListId, listTag, itemsInOrder.toString()));
        }

        // --- Redirect to show new comic ---
        return "redirect:/viewComic/" + userId + "/" + newComicId;
    }
}
