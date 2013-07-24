package org.semtag.core.model;

import java.util.LinkedHashMap;

/**
 * @author Ari Weiland
 */
public class ItemProperties {
    private final LinkedHashMap<String, Class<?>> properties;

    public ItemProperties() {
        this(new LinkedHashMap<String, Class<?>>());
    }

    public ItemProperties(LinkedHashMap<String, Class<?>> properties) {
        this.properties = properties;
    }

    public void addField(String name, Class<?> clazz) {
        properties.put(name, clazz);
    }

    public void removeField(String name) {
        properties.remove(name);
    }

    public void makeProperties(Item item, Object... values) {
        if (values == null || values.length != properties.size()) {
            throw new IllegalArgumentException("Incorrect amount of arguments");
        }
        for (Class<?> clazz : properties.values()) {
            if (values.getClass() != clazz) {
                throw new IllegalArgumentException("Incompatible types");
            }
        }

        // more stuff... how do I want to apply this to the Item?
    }
}
