package webcomicreader.webapp.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import webcomicreader.webapp.model.Comic;
import webcomicreader.webapp.model.ObjectWithId;
import webcomicreader.webapp.model.UserComic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An implementation of storage using Amazon SimpleDB and my (hardcoded) API keys.
 */
public class SimpleDBStorage implements StorageFacade {
    private final boolean SETUP_DATA = false;

    public final static String COMIC_DOMAIN = "comic";
    public final static String USER_COMIC_DOMAIN = "usercomic";

    private final AmazonSimpleDB client;

    /**
     * Constructor.
     */
    public SimpleDBStorage(String accessKey, String secretKey) {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.client = new AmazonSimpleDBClient(awsCredentials);

        if (SETUP_DATA) {
            client.createDomain(new CreateDomainRequest(COMIC_DOMAIN));
            client.putAttributes(new PutAttributesRequest(COMIC_DOMAIN, "1", new ArrayList<ReplaceableAttribute>() {{
                this.add(new ReplaceableAttribute("name", "XKCD", true));
                this.add(new ReplaceableAttribute("homepage", "https://www.xkcd.com/", true));
            }}));
            client.putAttributes(new PutAttributesRequest(COMIC_DOMAIN, "2", new ArrayList<ReplaceableAttribute>() {{
                this.add(new ReplaceableAttribute("name", "Schlock Mercenary", true));
                this.add(new ReplaceableAttribute("homepage", "http://www.schlockmercenary.com/", true));
            }}));
            client.putAttributes(new PutAttributesRequest(COMIC_DOMAIN, "3", new ArrayList<ReplaceableAttribute>() {{
                this.add(new ReplaceableAttribute("name", "Girl Genius", true));
                this.add(new ReplaceableAttribute("homepage", "http://www.girlgeniusonline.com/comic.php", true));
            }}));
            client.createDomain(new CreateDomainRequest(USER_COMIC_DOMAIN));
            client.putAttributes(new PutAttributesRequest(USER_COMIC_DOMAIN, "1-1", new ArrayList<ReplaceableAttribute>() {{
                this.add(new ReplaceableAttribute("user", "1", true));
                this.add(new ReplaceableAttribute("comic", "1", true));
                this.add(new ReplaceableAttribute("currentPosition", "https://www.xkcd.com/1153/", true));
            }}));
            client.putAttributes(new PutAttributesRequest(USER_COMIC_DOMAIN, "1-2", new ArrayList<ReplaceableAttribute>() {{
                this.add(new ReplaceableAttribute("user", "1", true));
                this.add(new ReplaceableAttribute("comic", "2", true));
                this.add(new ReplaceableAttribute("currentPosition", "http://www.schlockmercenary.com/2012-12-29", true));
            }}));
            client.putAttributes(new PutAttributesRequest(USER_COMIC_DOMAIN, "1-3", new ArrayList<ReplaceableAttribute>() {{
                this.add(new ReplaceableAttribute("user", "1", true));
                this.add(new ReplaceableAttribute("comic", "3", true));
                this.add(new ReplaceableAttribute("currentPosition", "http://www.girlgeniusonline.com/comic.php?date=20090911", true));
            }}));
        }
    }


    @Override
    public List<Comic> getComics() {
        // FIXME: Right now, this retrieves the comics for all users.
        List<Comic> result = new ArrayList<Comic>();
        String selectExpression = "select * from " + USER_COMIC_DOMAIN + " limit 100";
        SelectRequest selectRequest = new SelectRequest(selectExpression, false);
        SelectResult selectResult = client.select(selectRequest);
        List<Item> items = selectResult.getItems();
        for (Item userComicItem : items) {
            List<Attribute> comicAttributes = comicAttributes(userComicItem.getAttributes());
            result.add(new UserComicImpl(userComicItem.getName(), comicAttributes,  userComicItem.getAttributes()));
        }
        String nextToken = selectResult.getNextToken();
        if (nextToken != null) {
            throw new RuntimeException("Code not implemented for lists too large to read in one chunk.");
        }
        return result;
    }

    /**
     * Given a userComic item, this finds the attributes of the corresponding comic.
     */
    private List<Attribute> comicAttributes(List<Attribute> userComicAttributes) {
        String comicId = getComicId(userComicAttributes);
        return client.getAttributes(new GetAttributesRequest(COMIC_DOMAIN, comicId)).getAttributes();
    }

    /**
     * Given the attributes from a usercomic entry, this finds the ID of the comic and returns
     * it, or throws an exception if it cannot.
     */
    private String getComicId(List<Attribute> userComicAttributes) {
        for (Attribute a : userComicAttributes) {
            if ("comic".equals(a.getName())) {
                return a.getValue();
            }
        }
        throw new RuntimeException("Cannot find a comic for a usercomic.");
    }

    @Override
    public UserComic getComic(String id) {
        List<Attribute> userComicAttributes = client.getAttributes(new GetAttributesRequest(USER_COMIC_DOMAIN, id)).getAttributes();
        List<Attribute> comicAttributes = comicAttributes(userComicAttributes);
        return new UserComicImpl(id, comicAttributes, userComicAttributes);
    }

    @Override
    public void updateUserComic(UserComicSetter userComic) {
        updateSeveralFields(USER_COMIC_DOMAIN, userComic.getId(), userComic.getUserComicFields());
        updateSeveralFields(COMIC_DOMAIN, userComic.getComicId(), userComic.getComicFields());
    }

    /**
     * A private function that generates a new, unique ID.
     * <p>
     * FIXME: Not actually implemented yet.
     * @return the new, unique ID.
     */
    private String newID() {
        return "4";
    }

    @Override
    public void createComic(String name) {
        String itemName = newID();
        List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
        attributes.add(new ReplaceableAttribute("name", name, true));
        PutAttributesRequest putAttributesRequest = new PutAttributesRequest(COMIC_DOMAIN, itemName, attributes);
        client.putAttributes(putAttributesRequest);
    }

    @Override
    public void updateOneField(ObjectWithId obj, String fieldName, String fieldValue) {
        PutAttributesRequest putAttributesRequest = new PutAttributesRequest()
                .withDomainName(obj.getObjectType())
                .withItemName(obj.getId())
                .withAttributes(new ReplaceableAttribute(fieldName, fieldValue, true));
        client.putAttributes(putAttributesRequest);
    }

    @Override
    public void updateSeveralFields(ObjectWithId obj, Map<String, String> fieldsToUpdate) {
        updateSeveralFields(obj.getObjectType(), obj.getId(), fieldsToUpdate);
    }

    @Override
    public void updateSeveralFields(String domain, String id, Map<String, String> fieldsToUpdate) {
        List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>(fieldsToUpdate.size());
        for (Map.Entry<String, String> fieldEntry : fieldsToUpdate.entrySet()) {
            attributes.add(new ReplaceableAttribute(fieldEntry.getKey(), fieldEntry.getValue(), true));
        }
        PutAttributesRequest putAttributesRequest = new PutAttributesRequest()
                .withDomainName(domain)
                .withItemName(id)
                .withAttributes(attributes);
        client.putAttributes(putAttributesRequest);
    }
}
