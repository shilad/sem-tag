package org.semtag.core.model;

/**
 * @author Ari Weiland
 */
public class Item {
    private final String itemId;

    public Item(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
