package webcomicreader.webapp.storage.tempmemory;

/**
 * Implementation of User for tempmemory.
 */
public class UserImpl {
    private final String id;
    private final String username;

    public UserImpl(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
