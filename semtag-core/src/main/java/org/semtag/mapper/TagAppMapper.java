package org.semtag.mapper;

import org.apache.commons.lang.StringUtils;

/**
 * @author Ari Weiland
 */
public abstract class TagAppMapper {

    public TagAppMapper() {
    }

    /**
     * Normalizes a tag string, stripping
     * @param tag
     * @return
     */
    public String normalizeTag(String tag) {
        tag = StringUtils.replace(tag, "_", " ");
        tag = StringUtils.replace(tag, "-", " ");
        tag = tag.trim();
        tag = tag.toLowerCase();
        StringBuilder sb = new StringBuilder(tag.length());
        for(int i=0; i<tag.length(); i++) {
            char c = tag.charAt(i);
            if (StringUtils.isAlphanumericSpace(String.valueOf(c))) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

//    public abstract TagApp parseTagApp(User user, String tag, Item item, Timestamp timestamp);
}
