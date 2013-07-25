package org.semtag.core.model;

import java.sql.Timestamp;

/**
 * @author Ari Weiland
 */
public class TagApp {
    private final long tagAppId;
    private final User user;
    private final Tag tag;
    private final Item item;
    private final Timestamp timestamp;
    private final int conceptId;

    public TagApp(long tagAppId, User user, Tag tag, Item item, Timestamp timestamp, int conceptId) {
        this.tagAppId = tagAppId;
        this.user = user;
        this.tag = tag;
        this.item = item;
        this.timestamp = timestamp;
        this.conceptId = conceptId;
    }

    public long getTagAppId() {
        return tagAppId;
    }

    public User getUser() {
        return user;
    }

    public Tag getTag() {
        return tag;
    }

    public Item getItem() {
        return item;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getConceptId() {
        return conceptId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TagApp && this.tagAppId == ((TagApp) o).tagAppId;
    }
}
