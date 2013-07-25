package org.semtag.core.model;

import org.semtag.core.model.concept.Concept;

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

    private Concept concept;
    private int conceptId;

    public TagApp(long tagAppId, User user, Tag tag, Item item, Timestamp timestamp, int conceptId) {
        this.tagAppId = tagAppId;
        this.user = user;
        this.tag = tag;
        this.item = item;
        this.timestamp = timestamp;
        this.conceptId = conceptId;
    }

    public TagApp(long tagAppId, User user, Tag tag, Item item, Timestamp timestamp, Concept concept) {
        this.tagAppId = tagAppId;
        this.user = user;
        this.tag = tag;
        this.item = item;
        this.timestamp = timestamp;
        this.concept = concept;
        this.conceptId = concept.getConceptId();
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

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
        this.conceptId = concept.getConceptId();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TagApp && this.tagAppId == ((TagApp) o).tagAppId;
    }
}
