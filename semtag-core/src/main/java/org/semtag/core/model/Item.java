package org.semtag.core.model;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ari Weiland
 */
public class Item {
    private static final AtomicInteger itemIdGenerater = new AtomicInteger(0);

    private final int itemId;
    private final String name;
    private LinkedHashMap<Class<?>, Object> properties;

    public Item(String name) {
        this(name, new LinkedHashMap<Class<?>, Object>());
    }

    public Item(String name, LinkedHashMap<Class<?>, Object> properties) {
        this.itemId = itemIdGenerater.getAndIncrement();
        this.name = name;
        this.properties = properties;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public LinkedHashMap<Class<?>, Object> getProperties() {
        return properties;
    }

    public void setProperties(LinkedHashMap<Class<?>, Object> properties) {
        this.properties = properties;
    }
}
