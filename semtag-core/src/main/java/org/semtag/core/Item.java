package org.semtag.core;

import java.util.Map;

/**
 * @author Ari Weiland
 */
public class Item {
    private final int itemId;
    private final String name;
    private final Map<String, Field> properties;

    public Item(int itemId, String name, Map<String, Field> properties) {
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

    public Map<String, Field> getProperties() {
        return properties;
    }

    public void add(Field field) {
        properties.put(field.getName(), field);
    }

    public Field getProperty(String propName) {
        if (properties.containsKey(propName)) {
            return properties.get(propName);
        } else {
            return null;
        }
    }
}
