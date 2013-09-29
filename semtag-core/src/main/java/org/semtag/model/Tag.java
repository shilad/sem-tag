package org.semtag.model;

import org.semtag.model.concept.Concept;

/**
 * A simple class that wraps a tag string.
 * Maintains both the raw tag and a normalized tag
 * without any special characters or excess whitespace.
 *
 * @author Ari Weiland
 */
public class Tag {
    private final String rawTag;
    private final String normalizedTag;

    // these fields will be filled in iff
    // the tag is mapped or if it comes from the dao
    private final long tagId;
    private final int count;
    private Concept concept;


    public Tag(String rawTagString) {
        this.rawTag = rawTagString;
        this.normalizedTag = normalize(rawTagString);
        this.tagId = -1;
        this.count = 0;
    }


    public Tag(String rawTagString, long id, int count, Concept concept) {
        this.rawTag = rawTagString;
        this.normalizedTag = normalize(rawTagString);
        this.tagId = id;
        this.count = count;
        this.concept = concept;
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

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public boolean hasConcept() {
        return concept != null;
    }

    public long getTagId() {
        return tagId;
    }

    public int getCount() {
        return count;
    }

    public Concept getConcept() {
        return concept;
    }
}
