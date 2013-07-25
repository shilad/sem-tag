package org.semtag.core.model;

/**
 * @author Ari Weiland
 */
public class User {
    private final String userId;

    public User(int userId) {
        this.userId = String.valueOf(userId);
    }

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
