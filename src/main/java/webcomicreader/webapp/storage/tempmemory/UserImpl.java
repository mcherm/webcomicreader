package webcomicreader.webapp.storage.tempmemory;

import webcomicreader.webapp.model.ObjectWithId;

/**
 * Implementation of User for tempmemory.
 */
public class UserImpl implements ObjectWithId {
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
