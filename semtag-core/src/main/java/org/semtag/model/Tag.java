package org.semtag.model;

/**
 * @author Ari Weiland
 */
public class Tag {
    private final String rawTag;
    private final String normalizedTag;

    public Tag(String rawTagString) {
        this.rawTag = rawTagString;
        this.normalizedTag = normalize(rawTagString);
    }

    public String getRawTag() {
        return rawTag;
    }

    public String getNormalizedTag() {
        return normalizedTag;
    }

    public boolean isValid() {
        return !normalizedTag.isEmpty();
    }

    /**
     * Normalizes a tag string.
     * First it converts the tag to lower case.
     * Second it replaces all characters except digits and letters with spaces.
     * Third it trims whitespace from the ends.
     * Finally it truncates the string to a maximum length of 256 characters.
     * @param tag
     * @return
     */
    public static String normalize(String tag) {
        tag = tag.toLowerCase();
        tag = tag.replace('_', ' ');
        tag = tag.replaceAll("[^\\d\\w]+", " ");
        tag = tag.trim();
        if (tag.length() > 256) tag = tag.substring(0, 256);
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Tag && this.toString().equals(o.toString());
    }

    @Override
    public String toString() {
        return normalizedTag + " (" + rawTag + ")";
    }
}
