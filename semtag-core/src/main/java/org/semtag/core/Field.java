package org.semtag.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public class Field<V> {
    
    private final int fieldId;
    private final String name;
    private V value;

    /**
     * Constructs a field with a name, a value, and an arbitrarily generated ID.
     * It is the user's responsibility to choose unique names for their fields.
     * @param name
     * @param value
     */
    public Field(String name, V value) {
        this.fieldId = name.hashCode();
        this.name = name;
        this.value = value;
    }

    public int getFieldId() {
        return fieldId;
    }

    public String getName() {
        return name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Returns the boolean value of this field, or null if this field does not hold a boolean.
     * @return
     */
    public Boolean getBooleanValue() {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            return null;
        }
    }

    /**
     * Returns the int value of this field, or null if this field does not hold an int.
     * @return
     */
    public Integer getIntValue() {
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return null;
        }
    }

    /**
     * Returns the string value of this field, or null if this field does not hold a string.
     * @return
     */
    public String getStringValue() {
        if (value instanceof String) {
            return (String) value;
        } else {
            return null;
        }
    }

    /**
     * Returns a field that stores a boolean value.
     * @param name
     * @param value
     * @return
     */
    public static Field<Boolean> getBooleanField(String name, boolean value) {
        return new Field<Boolean>(name, value);
    }

    /**
     * Returns a field that stores an integer value.
     * @param name
     * @param value
     * @return
     */
    public static Field<Integer> getIntField(String name, int value) {
        return new Field<Integer>(name, value);
    }

    /**
     * Returns a field that stores a string value.
     * @param name
     * @param value
     * @return
     */
    public static Field<String> getTextField(String name, String value) {
        return new Field<String>(name, value);
    }
}
