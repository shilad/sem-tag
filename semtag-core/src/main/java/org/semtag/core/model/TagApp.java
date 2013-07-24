package org.semtag.core.model;

import org.semtag.core.model.concept.Concept;

import java.sql.Timestamp;

/**
 * @author Ari Weiland
 */
public class TagApp {
    private final int tagId;
    private final User user;
    private final String tag;
    private final Item item;
    private final Timestamp timestamp;

    private Concept concept;

    public TagApp(int tagId, User user, String tag, Item item, Timestamp timestamp) {
        this.tagId = tagId;
        this.user = user;
        this.tag = tag;
        this.item = item;
        this.timestamp = timestamp;
    }

    public int getTagId() {
        return tagId;
    }

    public User getUser() {
        return user;
    }

    public String getTag() {
        return tag;
    }

    public Item getItem() {
        return item;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }
}