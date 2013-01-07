package webcomicreader.webapp.storage.simpledb;

import com.amazonaws.services.simpledb.model.Attribute;
import webcomicreader.webapp.model.UserComic;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * An implementation of Comic that has all data initialized on creation.
 */
public class UserComicImpl implements UserComic {
    private final String userComicItemId;
    private final Map<String,String> fields;

    public UserComicImpl(String userComicId, List<Attribute> comicAttributes, List<Attribute> userComicAttributes) {
        this.userComicItemId = userComicId;
        this.fields = new TreeMap<String, String>();
        for (Attribute attribute : comicAttributes) {
            this.fields.put(attribute.getName(), attribute.getValue());
        }
        for (Attribute attribute : userComicAttributes) {
            this.fields.put(attribute.getName(), attribute.getValue());
        }
    }

    @Override
    public String getId() {
        return userComicItemId;
    }

    /**
     * A private method for dumping contents.
     */
    public Set<Map.Entry<String,String>> getAllFields() {
        return this.fields.entrySet();
    }

    @Override
    public String getObjectType() {
        return SimpleDBStorage.COMIC_DOMAIN;
    }

    @Override
    public String getName() {
        return fields.get("name");
    }


    @Override
    public String getHomepageURL() {
        return fields.get("homepage");
    }

    @Override
    public String getCurrentPositionURL() {
        return fields.get("currentPosition");
    }
}
