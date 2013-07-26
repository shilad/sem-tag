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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return userId.equals(user.userId);

    }

    @Override
    public String toString() {
        return userId;
    }
}
