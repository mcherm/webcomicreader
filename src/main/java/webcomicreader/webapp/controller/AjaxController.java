package webcomicreader.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import webcomicreader.webapp.storage.ComicListSetter;
import webcomicreader.webapp.storage.StorageFacade;

/**
 * A controller that has implementations of AJAX calls supported by this
 * application.
 */
@Controller
@RequestMapping("/ajax")
public class AjaxController {

    @Autowired
    private StorageFacade storage;


    /**
     * Perform an update to an existing ComicList.
     *
     * @param comicListId the ID of the ComicList to update
     * @param tagname the new name for the tag
     * @param itemsInOrder a space-separated list of the UserComic IDs
     */
    @RequestMapping(value = "/comicList/{comicListId}", method = RequestMethod.POST)
    public String comicListPut(
            @PathVariable String comicListId,
            @RequestParam("tagname") String tagname,
            @RequestParam("itemsInOrder") String itemsInOrder)
    {
        ComicListSetter data = new ComicListSetter(comicListId, tagname, itemsInOrder);
        storage.updateComicList(data);
        return "ajaxSuccess";
    }

}
