package org.semtag.core.model;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ari Weiland
 */
public class User {
    private static final AtomicInteger userIdGenerater = new AtomicInteger(0);

    private final int userId;
    private LinkedHashMap<Class<?>, Object> properties;

    public User(LinkedHashMap<Class<?>, Object> properties) {
        this.userId = userIdGenerater.incrementAndGet();
        this.properties = properties;
    }

    public int getUserId() {
        return userId;
    }

    public LinkedHashMap<Class<?>, Object> getProperties() {
        return properties;
    }

    public void setProperties(LinkedHashMap<Class<?>, Object> properties) {
        this.properties = properties;
    }
}
