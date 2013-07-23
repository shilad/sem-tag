package org.semtag.core;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ari Weiland
 */
public class User {
    private static final AtomicInteger userIdGenerater = new AtomicInteger(0);

    private final int userId;
    private final Map<String, Field> properties;

    public User(Map<String, Field> properties) {
        this.userId = userIdGenerater.incrementAndGet();
        this.properties = properties;
    }

    public int getUserId() {
        return userId;
    }

    public Map<String, Field> getProperties() {
        return properties;
    }

    public void add(Field field) {
        properties.put(field.getName(), field);
    }

}
