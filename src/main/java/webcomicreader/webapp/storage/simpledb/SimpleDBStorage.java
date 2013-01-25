package webcomicreader.webapp.storage.simpledb;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import webcomicreader.webapp.model.ComicList;
import webcomicreader.webapp.model.UserComic;
import webcomicreader.webapp.storage.ComicListSetter;
import webcomicreader.webapp.storage.StorageFacade;
import webcomicreader.webapp.storage.UserComicSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An implementation of storage using Amazon SimpleDB and my (hardcoded) API keys.
 */
public class SimpleDBStorage implements StorageFacade {

    public final static String ID_COUNT_DOMAIN = "idcount";
    public final static String USER_DOMAIN = "user";
    public final static String COMIC_DOMAIN = "comic";
    public final static String USER_COMIC_DOMAIN = "usercomic";
    public final static String COMICLIST_DOMAIN = "comiclist";

    private final IdGenerator idGenerator;
    private final AmazonSimpleDB client;

    /**
     * Constructor.
     */
    public SimpleDBStorage(String accessKey, String secretKey, boolean initialize_data) {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.client = new AmazonSimpleDBClient(awsCredentials);

        if (initialize_data) {
            client.createDomain(new CreateDomainRequest(ID_COUNT_DOMAIN));
            client.batchPutAttributes(new BatchPutAttributesRequest(ID_COUNT_DOMAIN, Arrays.asList(
                    // Key is always 0. Attributes map domain name to largest value used so far
                    new ReplaceableItem("0", Arrays.asList(
                            new ReplaceableAttribute(USER_DOMAIN, "1", true),
                            new ReplaceableAttribute(COMIC_DOMAIN, "3", true)
                    ))
            )));

            client.createDomain(new CreateDomainRequest(USER_DOMAIN));
            client.batchPutAttributes(new BatchPutAttributesRequest(USER_DOMAIN, Arrays.asList(
                    // Keys are "<user-id>"
                    new ReplaceableItem("1", Arrays.asList(
                            new ReplaceableAttribute("username", "mcherm", true)
                    ))
            )));

            client.createDomain(new CreateDomainRequest(COMIC_DOMAIN));
            client.batchPutAttributes(new BatchPutAttributesRequest(COMIC_DOMAIN, Arrays.asList(
                    // Keys are "<comic-id>"
                    new ReplaceableItem("1", Arrays.asList(
                            new ReplaceableAttribute("name", "XKCD", true),
                            new ReplaceableAttribute("homepage", "https://www.xkcd.com/", true)
                    )),
                    new ReplaceableItem("2", Arrays.asList(
                            new ReplaceableAttribute("name", "Schlock Mercenary", true),
                            new ReplaceableAttribute("homepage", "http://www.schlockmercenary.com/", true)
                    )),
                    new ReplaceableItem("3", Arrays.asList(
                            new ReplaceableAttribute("name", "Girl Genius", true),
                            new ReplaceableAttribute("homepage", "http://www.girlgeniusonline.com/comic.php", true)
                    ))
            )));

            client.createDomain(new CreateDomainRequest(USER_COMIC_DOMAIN));
            client.batchPutAttributes(new BatchPutAttributesRequest(USER_COMIC_DOMAIN, Arrays.asList(
                    // Keys are "<user-id>-<comic-id>"
                    new ReplaceableItem("1-1", Arrays.asList(
                            new ReplaceableAttribute("currentPosition", "https://www.xkcd.com/1153/", true)
                    )),
                    new ReplaceableItem("1-2", Arrays.asList(
                            new ReplaceableAttribute("currentPosition", "http://www.schlockmercenary.com/2012-12-29", true)
                    )),
                    new ReplaceableItem("1-3", Arrays.asList(
                            new ReplaceableAttribute("currentPosition", "http://www.girlgeniusonline.com/comic.php?date=20090911", true)
                    ))
            )));

            client.createDomain(new CreateDomainRequest(COMICLIST_DOMAIN));
            client.batchPutAttributes(new BatchPutAttributesRequest(COMICLIST_DOMAIN, Arrays.asList(
                    // Keys are "<user-id>-<tagname>"
                    new ReplaceableItem("1-all", Arrays.asList(
                            new ReplaceableAttribute("tagname", "all", true),
                            new ReplaceableAttribute("ordering", "L:1 3 2", true)
                    )),
                    new ReplaceableItem("1-reading", Arrays.asList(
                            new ReplaceableAttribute("tagname", "reading", true),
                            new ReplaceableAttribute("ordering", "L:2 3", true)
                    ))
            )));
        }

        this.idGenerator = new IdGenerator(this);
    }


    @Override
    public List<UserComic> getComics() {
        // FIXME: Right now, this retrieves the comics for all users.
        List<UserComic> result = new ArrayList<UserComic>();
        String selectExpression = "select * from " + USER_COMIC_DOMAIN + " limit 100";
        SelectRequest selectRequest = new SelectRequest(selectExpression, false);
        SelectResult selectResult = client.select(selectRequest);
        List<Item> items = selectResult.getItems();
        for (Item userComicItem : items) {
            String userComicId = userComicItem.getName();
            List<Attribute> comicAttributes = comicAttributes(userComicId, userComicItem.getAttributes());
            result.add(new UserComicImpl(userComicId, comicAttributes,  userComicItem.getAttributes()));
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
    private List<Attribute> comicAttributes(String userComicId, List<Attribute> userComicAttributes) {
        String comicId = getComicId(userComicId);
        return client.getAttributes(new GetAttributesRequest(COMIC_DOMAIN, comicId)).getAttributes();
    }

    /**
     * Determines the corresponding comicId for a given userComicId.
     *
     * @param userComicId the user-comic-id
     * @return the comic-id
     */
    public static String getComicId(String userComicId) {
        return userComicId.substring(userComicId.indexOf('-') + 1, userComicId.length());
    }

    @Override
    public UserComic getUserComic(String userComicId) {
        List<Attribute> userComicAttributes = client.getAttributes(new GetAttributesRequest(USER_COMIC_DOMAIN, userComicId)).getAttributes();
        List<Attribute> comicAttributes = comicAttributes(userComicId, userComicAttributes);
        return new UserComicImpl(userComicId, comicAttributes, userComicAttributes);
    }

    @Override
    public Collection<ComicList> getComicListsForUser(String userId) {
        // FIXME: Does not select for a particular user
        Collection<ComicList> result = new ArrayList<ComicList>();
        String selectExpression = "select * from " + COMICLIST_DOMAIN + " limit 100";
        SelectRequest selectRequest = new SelectRequest(selectExpression, false);
        SelectResult selectResult = client.select(selectRequest);
        List<Item> items = selectResult.getItems();
        for (Item comiclistItem : items) {
            result.add(new ComicListImpl(comiclistItem));
        }
        String nextToken = selectResult.getNextToken();
        if (nextToken != null) {
            throw new RuntimeException("Code not implemented for lists too large to read in one chunk.");
        }
        return result;
    }

    @Override
    public Map<String, UserComic> getUserComicsForUser(String userId) {
        Map<String,UserComic> result = new HashMap<String, UserComic>();
        for (UserComic userComic : getComics()) {
            result.put(userComic.getId(), userComic);
        }
        return result;
    }

    @Override
    public void updateUserComic(UserComicSetter userComic) {
        updateSeveralFields(USER_COMIC_DOMAIN, userComic.getId(), userComic.getUserComicFields());
        updateSeveralFields(COMIC_DOMAIN, userComic.getComicId(), userComic.getComicFields());
    }

    @Override
    public String createOrFindComic(String name, String homepage) {
        String newId = idGenerator.getNextId(COMIC_DOMAIN);
        Map<String,String> fields = new HashMap<String,String>();
        fields.put("name", name);
        fields.put("homepage", homepage);
        updateSeveralFields(COMIC_DOMAIN, newId, fields);
        return newId;
    }

    @Override
    public void createOrUpdateUserComic(String userId, String comicId, String currentPosition) {
        String userComicId = userId + "-" + comicId;
        client.putAttributes(new PutAttributesRequest(USER_COMIC_DOMAIN, userComicId, Arrays.asList(
                new ReplaceableAttribute("currentPosition", currentPosition, true)
        )));
    }

    @Override
    public void updateComicList(ComicListSetter comicList) {
        updateSeveralFields(COMICLIST_DOMAIN, comicList.getId(), comicList.getComicListFields());
    }

    @Override
    public void updateUserComicCurrentPosition(String userComicId, final String currentPosition) {
        updateSeveralFields(USER_COMIC_DOMAIN, userComicId,
                new TreeMap<String,String>() {{put("currentPosition", currentPosition);}});
    }

    private void updateSeveralFields(String domain, String id, Map<String, String> fieldsToUpdate) {
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


    /**
     * Finds the values previously stored which should contain the largest
     * ID value already used in the database.
     *
     * @return a map from domain name to largest value already used as
     *   an ID. Only returns the domains that have integer IDs.
     */
    public Map<String,Integer> getLargestIdsUsed() {
        Map<String,Integer> result = new TreeMap<String, Integer>();
        GetAttributesResult data = client.getAttributes(new GetAttributesRequest(ID_COUNT_DOMAIN, "0"));
        for (Attribute attribute : data.getAttributes()) {
            String domainName = attribute.getName();
            Integer largestUsed = Integer.parseInt(attribute.getValue());
            result.put(domainName, largestUsed);
        }
        return result;
    }

    /**
     * This is used to update the values for largest Ids used. It is passed
     * a single value for a domain and a new largest-used ID.
     */
    public void setLargestIdUsed(String domainName, int newLargestValue) {
        client.putAttributes(new PutAttributesRequest(ID_COUNT_DOMAIN, "0", Arrays.asList(
                new ReplaceableAttribute(domainName, Integer.toString(newLargestValue), true)
        )));
    }

}
