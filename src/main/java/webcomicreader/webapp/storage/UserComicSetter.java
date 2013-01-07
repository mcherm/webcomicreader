package webcomicreader.webapp.storage;

import org.springframework.util.MultiValueMap;
import webcomicreader.webapp.model.UserComic;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An implementation of UserComic that is constructed from fields.
 * <p>
 * FIXME: I think I can use spring to build this, but let me try it manually first.
 * FIXME: This may not need to implement UserComic.
 */
public class UserComicSetter implements UserComic {
    private final String id;
    private final String name;
    private final String homepage;
    private final String currentPosition;

    /**
     * Constructor which is passed a MultiValueMap from a HTML POST.
     * @param fields the HTML POST
     */
    public UserComicSetter(String id, MultiValueMap<String,String> fields) {
        this.id = id;
        name = onlyValue(fields, "name");
        homepage = onlyValue(fields, "homepage");
        currentPosition = onlyValue(fields, "currentPosition");
    }

    /**
     * Extracts a single value from a MultiValueMap. If no value exists, it
     * return null. If multiple values exist it throws an exception.
     * @return the single value
     */
    private String onlyValue(MultiValueMap<String,String> fields, String fieldName) {
        for (Map.Entry<String, List<String>> entry : fields.entrySet()) {
            if (fieldName.equals(entry.getKey())) {
                if (entry.getValue() == null || entry.getValue().size() == 0) {
                    return null;
                } else if (entry.getValue().size() > 1) {
                    throw new RuntimeException("Got multiple values in POST for '" + fieldName + "'.");
                } else {
                    return entry.getValue().get(0);
                }
            }
        }
        return null;
    }

    @Override
    public String getCurrentPositionURL() {
        return currentPosition;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHomepageURL() {
        return homepage;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getObjectType() {
        // FIXME: I think this method shouldn't be needed at all
        return null;
    }

    /**
     * Method used for writing this to the DB.
     * @return the ID of the corresponding comic.
     */
    public String getComicId() {
        return id.substring(id.indexOf('-') + 1, id.length());
    }

    /**
     * Method used for writing this to the DB.
     */
    public Map<String,String> getComicFields() {
        return new TreeMap<String,String>() {{
            put("name", name);
            put("homepage", homepage);
        }};
    }

    /**
     * Method used for writing this to the DB.
     */
    public Map<String,String> getUserComicFields() {
        return new TreeMap<String,String>() {{
            put("currentPosition", currentPosition);
        }};
    }

}
