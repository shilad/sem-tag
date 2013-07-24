package org.semtag.core.model;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ari Weiland
 */
public class User {
    private static final AtomicInteger userIdGenerater = new AtomicInteger(0);

    private final int userId;
    private final int name;
    private LinkedHashMap<Class<?>, Object> properties;

    public User(int name, LinkedHashMap<Class<?>, Object> properties) {
        this.userId = userIdGenerater.getAndIncrement();
        this.name = name;
        this.properties = properties;
    }

    public int getUserId() {
        return userId;
    }

    public int getName() {
        return name;
    }

    public LinkedHashMap<Class<?>, Object> getProperties() {
        return properties;
    }

    public void setProperties(LinkedHashMap<Class<?>, Object> properties) {
        this.properties = properties;
    }
}
