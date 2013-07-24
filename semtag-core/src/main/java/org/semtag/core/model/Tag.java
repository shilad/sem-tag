package org.semtag.core.model;

import org.apache.commons.lang.StringUtils;

/**
 * @author Ari Weiland
 */
public class Tag {
    private final String rawTag;
    private final String normalizedTag;

    public Tag(String tag) {
        this.rawTag = tag;
        this.normalizedTag = normalize(tag);
    }

    public String getRawTag() {
        return rawTag;
    }

    public String getNormalizedTag() {
        return normalizedTag;
    }

    /**
     * Normalizes a tag string, stripping all
     * First it replaces underscore and hyphen with spaces.
     * Second it converts the tag to lower case.
     * Third it removes all characters except digits, letters, and whitespace.
     * Fourth it trims whitespace from the ends.
     * Finally it replaces all substrings of whitespace with a single space.
     * @param tag
     * @return
     */
    public static String normalize(String tag) {
        tag = StringUtils.replace(tag, "_", " ");
        tag = StringUtils.replace(tag, "-", " ");
        tag = tag.toLowerCase();
        tag = tag.replaceAll("[^\\d\\w\\s]", "");
        tag = tag.trim();
        tag = tag.replaceAll("[\\s]+", " ");
        return tag;
    }

    @Override
    public String toString() {
        return normalizedTag;
    }
}
