package org.semtag.model;

/**
 * An item in a tagging system.
 *
 * @author Ari Weiland
 */
public class Item {
    private final String itemId;

    /**
     * Constructs an item from a given ID.
     * @param itemId
     */
    public Item(long itemId) {
        this.itemId = String.valueOf(itemId);
    }

    /**
     * Constructs an item from a given ID.
     * @param itemId
     */
    public Item(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        return itemId.equals(item.itemId);
    }

    @Override
    public int hashCode() {
        return itemId.hashCode();
    }

    @Override
    public String toString() {
        return itemId;
    }
}
