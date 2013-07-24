package org.semtag.core.model;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/**
 * This class should be constructed and assembled before any load process,
 * as it will be needed to construct the SQL databases. Each implementation
 * of this library should use one ItemProperties class per database, and
 * that class must be consistent throughout use of that database.
 * 
 * @author Ari Weiland
 */
public class ItemProperties {
    private final LinkedHashMap<String, Class<?>> properties;

    /**
     * Constructs a new empty list of properties.
     */
    public ItemProperties() {
        this(new LinkedHashMap<String, Class<?>>());
    }

    /**
     * Constructs a list of properties initialized with the input properties.
     * 
     * @param properties
     */
    public ItemProperties(LinkedHashMap<String, Class<?>> properties) {
        this.properties = properties;
    }

    /**
     * Adds a new property to the list of properties.
     * 
     * @param name  the name of the property. 
     *              This will also be the name of the equivalent SQL field
     * @param clazz the class of the property
     */
    public void addProperty(String name, Class<?> clazz) {
        properties.put(name, clazz);
    }

    /**
     * Removes the specified property from the list of properties.
     * 
     * @param name
     */
    public void removeProperty(String name) {
        properties.remove(name);
    }

    /**
     * Returns the properties map.
     * @return
     */
    public Map<String, Class<?>> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Assembles the necessary SQL files with the specified tableName.
     * 
     * @param tableName the name for the table and the prefix for the files
     * @param indexProperties indicates whether integer-based properties should be indexed
     * @throws IOException
     */
    public void buildSqlFiles(String tableName, boolean indexProperties) throws IOException {
        File db = new File("src/main/resources/db/");
        if (!db.isDirectory()) db.mkdirs();

        // writes the create-tables SQL file
        File createTables = new File(db, tableName + "-create-tables.sql");
        createTables.delete();
        createTables.createNewFile();
        List<String> lines = new ArrayList<String>();
        lines.add("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        lines.add("  item_id INT NOT NULL,");
        lines.add("  name VARCHAR NOT NULL,");
        for (Map.Entry<String, Class<?>> entry : properties.entrySet()) {
            String type = getSqlType(entry.getValue());
            lines.add("  " + entry.getKey() + " " + type + "NOT NULL,");
        }
        int last = lines.size() - 1;
        String lastLine = lines.get(last);
        lines.remove(last);
        lines.add(StringUtils.strip(lastLine, ","));
        lines.add(");");
        FileUtils.writeLines(createTables, lines, "\n");

        // writes the drop-tables SQL file
        File dropTables = new File(db, tableName + "-drop-tables.sql");
        dropTables.delete();
        dropTables.createNewFile();
        lines = new ArrayList<String>();
        lines.add("DROP TABLE IF EXISTS " + tableName + ";");
        FileUtils.writeLines(dropTables, lines, "\n");

        // writes the create- and drop-index SQL files, 
        // including integer field indexes if specified
        File createIndexes = new File(db, tableName + "-create-indexes.sql");
        File dropIndexes = new File(db, tableName + "-drop-indexes.sql");
        createIndexes.delete();
        createIndexes.createNewFile();
        dropIndexes.delete();
        dropIndexes.createNewFile();
        lines = new ArrayList<String>();
        List<String> lines2 = new ArrayList<String>();
        lines.add("CREATE INDEX IF NOT EXISTS " + tableName + "_idx_item_id ON " + tableName + "(item_id);");
        lines2.add("DROP INDEX IF EXISTS " + tableName + "_idx_item_id;");
        if (indexProperties) {
            for (Map.Entry<String, Class<?>> entry : properties.entrySet()) {
                String name = entry.getKey();
                Class<?> clazz = entry.getValue();
                if (    clazz == Byte.class || clazz == byte.class ||
                        clazz == Short.class || clazz == short.class ||
                        clazz == Integer.class || clazz == int.class ||
                        clazz == Long.class || clazz == long.class) {
                    lines.add("CREATE INDEX IF NOT EXISTS " + tableName + "_idx_" + name +
                            " ON " + tableName + "(item_id, " + name +");");
                    lines2.add("DROP INDEX IF EXISTS " + tableName + "_idx_" + name + ";");
                }
            }
        }
        FileUtils.writeLines(createIndexes, lines, "\n");
        FileUtils.writeLines(dropIndexes, lines2, "\n");
    }

    /**
     * Generates a LinkedHashMap of properties to be applied to an Item.
     * Input values must have a one-to-one matching relationship to the
     * properties applied to this instance of ItemProperties.
     *
     * @param values
     * @return
     * @throws IllegalArgumentException if the values
     */
    public LinkedHashMap<Class<?>, Object> makeProperties(Object... values) {
        if (values == null || values.length != properties.size()) {
            throw new IllegalArgumentException("Incorrect amount of arguments");
        }
        LinkedHashMap<Class<?>, Object> madeProps = new LinkedHashMap<Class<?>, Object>(values.length);
        int i=0;
        for (Class<?> clazz : properties.values()) {
            if (values[i].getClass() != clazz) {
                throw new IllegalArgumentException("Incompatible types");
            } else {
                madeProps.put(clazz, values[i]);
            }
            i++;
        }
        return madeProps;
    }

    /**
     * Returns the H2 SQL data type name of the specified class.
     *
     * @param clazz
     * @return
     */
    public static String getSqlType(Class<?> clazz) {
        if (    clazz == Integer.class || clazz == int.class ||
                clazz == Boolean.class || clazz == boolean.class ||
                clazz == Double.class || clazz == double.class ||
                clazz == Time.class ||
                clazz == Date.class ||
                clazz == Timestamp.class ||
                clazz == Blob.class ||
                clazz == Clob.class) {
            return clazz.getSimpleName().toUpperCase();
        } else if (clazz == Byte.class || clazz == byte.class) {
            return "TINYINT";
        } else if (clazz == Short.class || clazz == short.class) {
            return "SMALLINT";
        } else if (clazz == Long.class || clazz == long.class) {
            return "BIGINT";
        } else if (clazz == BigDecimal.class) {
            return "DECIMAL";
        } else if (clazz == Float.class || clazz == float.class) {
            return "REAL";
        } else if (clazz == Byte[].class || clazz == byte[].class) {
            return "BINARY";
        } else if (clazz == String.class) {
            return "VARCHAR";
        } else if (clazz.isArray()) {
            return "ARRAY";
        } else {
            return "OTHER";
        }
    }
}
