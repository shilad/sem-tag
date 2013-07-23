package org.semtag.mapper;

import org.apache.commons.lang.StringUtils;
import org.semtag.core.Item;
import org.semtag.core.TagApp;
import org.semtag.core.User;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ari Weiland
 */
public abstract class TagAppMapper {

    protected static final AtomicInteger TAGAPP_ID_COUNTER = new AtomicInteger(0);

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

    public abstract TagApp mapTagApp(User user, String tag, Item item, Timestamp timestamp);
}
