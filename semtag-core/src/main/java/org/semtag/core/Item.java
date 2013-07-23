package org.semtag.core;

import java.util.Map;

/**
 * @author Ari Weiland
 */
public class Item {
    private final int itemId;
    private final String itemName;
    private final Map<String, Field> properties;

    public Item(int itemId, String itemName, Map<String, Field> properties) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.properties = properties;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public Map<String, Field> getProperties() {
        return properties;
    }
}
