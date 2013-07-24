package org.semtag.core.model;

import java.util.LinkedHashMap;

/**
 * @author Ari Weiland
 */
public class Item {
    private final int itemId;
    private final String name;
    private LinkedHashMap<Class<?>, Object> properties;

    public Item(int itemId, String name) {
        this(itemId, name, new LinkedHashMap<Class<?>, Object>());
    }

    public Item(int itemId, String name, LinkedHashMap<Class<?>, Object> properties) {
        this.itemId = itemId;
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
